package com.blacklog.filestorage.exception;

import org.springframework.http.HttpStatus;

public class DownloadFileException extends ApiException {
	public DownloadFileException(String message) {
		super("DOWNLOAD_FAIL", message, HttpStatus.NOT_FOUND);
	}
}
