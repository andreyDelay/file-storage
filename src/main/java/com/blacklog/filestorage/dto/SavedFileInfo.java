package com.blacklog.filestorage.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SavedFileInfo {
	private String filename;
	private String filepath;
	private long size;
}