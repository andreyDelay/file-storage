package com.blacklog.filestorage.controller;

import com.blacklog.filestorage.dto.SavedFileInfo;
import com.blacklog.filestorage.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileStorageController {

	private final FileStorageService fileStorageService;

	@PostMapping("/upload")
	@ResponseStatus(HttpStatus.CREATED)
	public SavedFileInfo uploadFile(@RequestParam("file") MultipartFile file) {
		return fileStorageService.saveFile(file);
	}

	@PostMapping(value = "/download",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public Resource downloadFile(@RequestBody SavedFileInfo fileInfo) {
		return fileStorageService.downloadFile(fileInfo);
	}
}
