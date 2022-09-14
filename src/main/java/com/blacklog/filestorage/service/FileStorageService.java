package com.blacklog.filestorage.service;

import com.blacklog.filestorage.dto.SavedFileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


public interface FileStorageService {
	SavedFileInfo saveFile(MultipartFile multipartFile);
	File downloadFile(String filepath);
}
