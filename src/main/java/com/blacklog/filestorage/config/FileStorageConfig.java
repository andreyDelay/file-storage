package com.blacklog.filestorage.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "storage.details")
public class FileStorageConfig {
	private String parentFolder;
	private String baseTpFolder;
	private String baseTpFilename;
}
