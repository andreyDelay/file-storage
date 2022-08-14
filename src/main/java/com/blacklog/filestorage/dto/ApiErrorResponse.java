package com.blacklog.filestorage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
	private String code;
	private String message;

	public ApiErrorResponse(Enum<?> code, String message) {
		this.code = code.name();
		this.message = message;
	}
}
