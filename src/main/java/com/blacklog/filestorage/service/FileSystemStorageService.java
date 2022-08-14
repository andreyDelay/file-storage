package com.blacklog.filestorage.service;

import com.blacklog.filestorage.config.FileStorageConfig;
import com.blacklog.filestorage.dto.SavedFileResponse;
import com.blacklog.filestorage.exception.DownloadFileException;
import com.blacklog.filestorage.exception.FileNotFoundException;
import com.blacklog.filestorage.exception.InvalidFileException;
import com.blacklog.filestorage.exception.UploadFileException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileSystemStorageService implements FileStorageService {

	private final FileStorageConfig storageConfig;

	@Override
	public SavedFileResponse saveFile(String targetFolder, MultipartFile multipartFile) {
		if (Objects.isNull(multipartFile.getOriginalFilename())) {
			throw new InvalidFileException("File name is not valid.");
		}

		var filename = String.join(
				".",
				storageConfig.getBaseTpFilename(),
				FilenameUtils.getExtension(multipartFile.getOriginalFilename()));

		var destinationDirectory = getDestinationDirectory(targetFolder);

		try (InputStream is = multipartFile.getInputStream()) {
			Path filepath = destinationDirectory.resolve(filename);

			if (!Files.exists(filepath)) {
				Files.createDirectories(filepath);
			}

			Files.copy(is, filepath, StandardCopyOption.REPLACE_EXISTING);
			return SavedFileResponse.builder()
					.size(multipartFile.getSize())
					.filename(filename)
					.url(filepath.normalize().toString())
					.build();
		} catch (IOException e) {
			throw new UploadFileException(String.format("Couldn't save a file, error: %s", e.getMessage()));
		}
	}

	@Override
	public Resource downloadFile(String filename) {
		var requiredFilename = storageConfig.getBaseTpFilename();
		var destinationDirectory = new File(storageConfig.getParentFolder());

		try {
			URI uri = Files.find(destinationDirectory.toPath(), Integer.MAX_VALUE,
							((p, basicFileAttributes) -> p.getFileName().toString().contains(requiredFilename)))
					.findFirst()
					.map(Path::toUri)
					.orElseThrow(() -> new FileNotFoundException("File not found in target directory."));
			return new UrlResource(uri);
		} catch (IOException e) {
			throw new DownloadFileException(String.format("Cannot get required file, error: %s", e.getMessage()));
		}
	}

	private Path getDestinationDirectory(String targetFolder) {
		return Paths.get(storageConfig.getParentFolder(), targetFolder);
	}
}
