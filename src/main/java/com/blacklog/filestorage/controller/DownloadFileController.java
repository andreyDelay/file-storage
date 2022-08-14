package com.blacklog.filestorage.controller;

import com.blacklog.filestorage.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/download")
@RequiredArgsConstructor
public class DownloadFileController {

	private final FileStorageService fileStorageService;

	@GetMapping("/{filename}")
	@ResponseStatus(HttpStatus.OK)
	public Resource downloadFile(@PathVariable String filename) {
		return fileStorageService.downloadFile(filename);
	}
}
