package com.blacklog.filestorage.service;

import com.blacklog.filestorage.config.FileStorageProperties;
import com.blacklog.filestorage.dto.SavedFileInfo;
import com.blacklog.filestorage.exception.DownloadFileException;
import com.blacklog.filestorage.exception.InvalidFileException;
import com.blacklog.filestorage.exception.UploadFileException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileSystemStorageService implements FileStorageService {

	private final FileStorageProperties storageProperties;

	@Override
	public SavedFileInfo saveFile(MultipartFile multipartFile) {
		log.info("In saveFile method.");
		if (!isFileValid(multipartFile)) {
			log.error("Wrong filename or extension - {}. Should be like test.xlsx", multipartFile.getOriginalFilename());
			throw new InvalidFileException("File name is not valid.");
		}

		try (InputStream is = multipartFile.getInputStream()) {
			String fileName = multipartFile.getOriginalFilename();
			Path filepath = Path.of(storageProperties.getParentFolder(), fileName);

			if (!Files.exists(filepath)) {
				Files.createDirectories(filepath);
			}

			Files.copy(is, filepath, StandardCopyOption.REPLACE_EXISTING);

			log.info("File with filename {}, has been saved.", fileName);
			return SavedFileInfo.builder()
					.size(multipartFile.getSize())
					.filename(fileName)
					.filepath(filepath.normalize().toString())
					.build();
		} catch (IOException e) {
			log.error("Unexpected error, file haven't been saved. Error: {}", e.getMessage());
			throw new UploadFileException(String.format("Couldn't save a file, error: %s", e.getMessage()));
		}
	}

	@Override
	public File downloadFile(String filepath) {
		log.info("In downloadFile method.");
		if (Objects.isNull(filepath)) {
			log.error("Error - wrong filepath.");
			throw new DownloadFileException("The path to the file is not specified.");
		}

		File targetFile = new File(filepath);
		if (!targetFile.exists()) {
			log.error("Path to a file incorrect, the file by this path not exists. Path: {}", targetFile.getPath());
			throw new DownloadFileException(String.format("File with path - %s not found.", filepath));
		}

		log.info("File with filename {}, has been found and will returned.", targetFile.getName());
		return targetFile;
	}

	private boolean isFileValid(MultipartFile multipartFile) {
		String originalFilename = multipartFile.getOriginalFilename();
		if (Objects.isNull(originalFilename) || originalFilename.length() == 0) {
			return false;
		}
		String fileExtension = FilenameUtils.getExtension(originalFilename);

		return fileExtension.equalsIgnoreCase(storageProperties.getRequiredFileExtension());
	}
}
