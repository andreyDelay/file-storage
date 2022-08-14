package com.blacklog.filestorage.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SavedFileResponse {
	private String filename;
	private String url;
	private long size;
}