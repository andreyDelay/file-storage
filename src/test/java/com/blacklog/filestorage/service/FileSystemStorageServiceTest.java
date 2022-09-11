package com.blacklog.filestorage.service;

import com.blacklog.filestorage.config.FileStorageProperties;
import com.blacklog.filestorage.dto.SavedFileInfo;
import com.blacklog.filestorage.exception.InvalidFileException;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileSystemStorageServiceTest {

	private FileStorageService storageService;

	public FileSystemStorageServiceTest() {
		FileStorageProperties storageProperties = new FileStorageProperties();
		storageProperties.setRequiredFileExtension("xlsx");
		storageProperties.setParentFolder("file-storage");
		this.storageService = new FileSystemStorageService(storageProperties);
	}

	@Test
	void shouldThrowExceptionWhenFileExtensionNotPresent() {
		MockMultipartFile multipartFile = new MockMultipartFile("test", "test".getBytes());
		assertThrows(InvalidFileException.class, () -> storageService.saveFile(multipartFile));
	}

	@Test
	void shouldThrowExceptionWhenFilenameNotSpecified() {
		MockMultipartFile multipartFile = new MockMultipartFile(" ", "test".getBytes());
		assertThrows(InvalidFileException.class, () -> storageService.saveFile(multipartFile));
	}

	@Test
	void shouldSaveFile() throws IOException {
		//given
		MockMultipartFile multipartFile =
				new MockMultipartFile("1.xlsx","1.xlsx", MediaType.TEXT_PLAIN_VALUE, "test".getBytes());
		//when
		SavedFileInfo savedFileInfo = storageService.saveFile(multipartFile);

		//then
		assertEquals(multipartFile.getSize(),  savedFileInfo.getSize());

		//given
		String filepath = savedFileInfo.getFilepath();

		//when
		File file = storageService.downloadFile(filepath);

		//then
		assertEquals(multipartFile.getName(), file.getName());
		assertEquals(multipartFile.getSize(), Files.size(file.toPath()));

		Files.delete(file.toPath());
	}
}