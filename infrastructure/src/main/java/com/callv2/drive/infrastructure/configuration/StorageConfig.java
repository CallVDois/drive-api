package com.callv2.drive.infrastructure.configuration;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.drive.infrastructure.configuration.properties.storage.FileSystemStorageProperties;
import com.callv2.drive.infrastructure.storage.FileSystemStorage;

@Configuration
public class StorageConfig {

    @Bean
    FileSystemStorage fileSystemStorage(final FileSystemStorageProperties properties) {
        final Path location = Path.of(properties.getLocation());
        return new FileSystemStorage(location);
    }

    @Bean
    @ConfigurationProperties("storage.file-system")
    FileSystemStorageProperties fileSystemStorageProperties() {
        return new FileSystemStorageProperties();
    }

}
