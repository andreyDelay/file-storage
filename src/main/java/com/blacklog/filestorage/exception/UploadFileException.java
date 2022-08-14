package com.blacklog.filestorage.exception;

import org.springframework.http.HttpStatus;

public class UploadFileException extends ApiException {
	public UploadFileException(String message) {
		super("SAVING_FILE_ERROR", message, HttpStatus.SERVICE_UNAVAILABLE);
	}
}
