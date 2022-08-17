package com.blacklog.filestorage.service;

import com.blacklog.filestorage.dto.SavedFileInfo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public interface FileStorageService {
	SavedFileInfo saveFile(MultipartFile multipartFile);
	Resource downloadFile(SavedFileInfo fileInfo);
}
