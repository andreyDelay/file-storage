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

	@PostMapping(value = "/upload",
				consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public SavedFileInfo uploadFile(@RequestPart() MultipartFile file) {
		return fileStorageService.saveFile(file);
	}

	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public Resource downloadFile(@RequestParam String filepath) {
		return fileStorageService.downloadFile(filepath);
	}
}
