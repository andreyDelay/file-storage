package com.blacklog.filestorage.service;

import com.blacklog.filestorage.dto.SavedFileResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
	SavedFileResponse saveFile(String targetFolder, MultipartFile multipartFile);
	Resource downloadFile(String filename);
}
