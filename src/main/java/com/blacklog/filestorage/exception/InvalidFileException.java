package com.blacklog.filestorage.exception;

import org.springframework.http.HttpStatus;

public class InvalidFileException extends ApiException {
	public InvalidFileException(String message) {
		super("INVALID_FILE", message, HttpStatus.UNPROCESSABLE_ENTITY);
	}
}
