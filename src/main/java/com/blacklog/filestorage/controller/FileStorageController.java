package com.blacklog.filestorage.controller;

import com.blacklog.filestorage.dto.SavedFileInfo;
import com.blacklog.filestorage.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileStorageController {

	private final FileStorageService fileStorageService;

	@PostMapping(value = "/upload",
				consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public SavedFileInfo uploadFile(@RequestPart() MultipartFile file) {
		return fileStorageService.saveFile(file);
	}

	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Resource> downloadFile(@RequestParam("filepath") String filepath) {
		File file = fileStorageService.downloadFile(filepath);
		String filename = file.getName();

		ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
				.filename(filename, StandardCharsets.UTF_8)
				.build();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(contentDisposition);

		try {
			ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(file.toPath()));
			return ResponseEntity.ok()
					.headers(headers)
					.contentLength(resource.contentLength())
					.contentType(MediaType.parseMediaType("application/octet-stream"))
					.body(resource);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
