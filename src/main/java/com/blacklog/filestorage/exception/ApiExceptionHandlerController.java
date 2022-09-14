package com.blacklog.filestorage.exception;

import com.blacklog.filestorage.dto.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandlerController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<?> handleException(ApiException exception) {
		ApiErrorResponse errorResponse = new ApiErrorResponse(exception.getCode(), exception.getMessage());
		return new ResponseEntity<>(errorResponse, exception.getHttpStatus());
	}
}
