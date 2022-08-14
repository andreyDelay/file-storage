package com.blacklog.filestorage.controller;

import com.blacklog.filestorage.config.FileStorageConfig;
import com.blacklog.filestorage.dto.SavedFileResponse;
import com.blacklog.filestorage.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class UploadFileController {

	private final FileStorageService fileStorageService;
	private final FileStorageConfig fileStorageConfig;

	@PostMapping("/basetp")
	@ResponseStatus(HttpStatus.CREATED)
	public SavedFileResponse uploadBaseTp(@RequestParam("file") MultipartFile file) {
		String baseTpFolder = fileStorageConfig.getBaseTpFolder();
		return fileStorageService.saveFile(baseTpFolder, file);
	}
}
