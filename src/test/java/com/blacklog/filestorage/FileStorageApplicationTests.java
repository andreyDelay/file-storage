package com.blacklog.filestorage;

import com.blacklog.filestorage.config.FileStorageProperties;
import com.blacklog.filestorage.controller.FileStorageController;
import com.blacklog.filestorage.dto.SavedFileInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FileStorageApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private FileStorageController storageController;

	@Autowired
	private FileStorageProperties storageProperties;

	@Value("${api.url}")
	private String controllerUrl;

	private static final String testFilePath = "file-storage/testFilePath";

	@BeforeAll
	static void beforeAll() throws IOException {
		Path createdFilePath = Files.createFile(Paths.get(testFilePath));
		FileWriter fileWriter = new FileWriter(createdFilePath.toString());
		fileWriter.write("any content for test file");
		fileWriter.close();
	}

	@AfterAll
	static void afterAll() throws IOException {
		Files.delete(Paths.get(testFilePath));
	}

	@Test
	void contextLoads() {
		assertThat(storageController).isNotNull();
		assertThat(mockMvc).isNotNull();
	}

	@Test
	void controllerShouldSaveFile() throws Exception {
		//given
		MockMultipartFile mockMultipartFile =
				new MockMultipartFile("file", "test.xlsx",
						MediaType.APPLICATION_OCTET_STREAM_VALUE,
						"test.xlsx".getBytes());
		//when
		mockMvc.perform(multipart(controllerUrl + "/upload")
						.file(mockMultipartFile))
				.andDo(print())
		//then
				.andExpectAll(
						status().isCreated(),
						content().contentType(MediaType.APPLICATION_JSON),
						jsonPath("$.size").value(mockMultipartFile.getSize()),
						jsonPath("$.filename").value(mockMultipartFile.getOriginalFilename()));

		Files.delete(Paths.get(storageProperties.getParentFolder(), mockMultipartFile.getOriginalFilename()));
	}

	@Test
	void controllerShouldDownloadFile() throws Exception {
		//given
		File file = new File(testFilePath);
		String filename = file.getName();
		String filePath = file.getPath();
		long fileSize = Files.size(file.toPath());

		SavedFileInfo requiredFileDetails = SavedFileInfo.builder()
				.filename(filename)
				.filepath(filePath)
				.size(fileSize)
				.build();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonBody = objectMapper.writeValueAsString(requiredFileDetails);

		//when
		MvcResult mvcResult = mockMvc.perform(post(controllerUrl + "/download")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonBody))
				.andDo(print())
		//then
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
				.andReturn();

		long responseContentLength = mvcResult.getResponse().getContentAsByteArray().length;
		assertThat(responseContentLength).isCloseTo(fileSize, Percentage.withPercentage(95.0));
	}

}
