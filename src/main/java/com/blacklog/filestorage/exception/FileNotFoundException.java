package com.blacklog.filestorage.exception;

import org.springframework.http.HttpStatus;

public class FileNotFoundException extends ApiException {
	public FileNotFoundException(String message) {
		super("FILE_NOT_FOUND", message, HttpStatus.NOT_ACCEPTABLE);
	}
}
