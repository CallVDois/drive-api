package com.callv2.drive.infrastructure.configuration;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.drive.domain.storage.StorageService;
import com.callv2.drive.infrastructure.configuration.properties.storage.FileSystemStorageProperties;
import com.callv2.drive.infrastructure.storage.FileSystemStorageService;

@Configuration
public class StorageConfig {

    @Bean
    StorageService storageService(final FileSystemStorageProperties properties) {

        final Path location = Path.of(properties.getLocation());

        return new FileSystemStorageService(location);

    }

    @Bean
    @ConfigurationProperties("storage.file-system")
    FileSystemStorageProperties fileSystemStorageProperties() {
        return new FileSystemStorageProperties();
    }

}
