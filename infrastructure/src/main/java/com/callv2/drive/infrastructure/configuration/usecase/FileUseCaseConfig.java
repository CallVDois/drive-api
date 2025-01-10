package com.callv2.drive.infrastructure.configuration.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.drive.application.file.create.CreateFileUseCase;
import com.callv2.drive.application.file.create.DefaultCreateFileUseCase;
import com.callv2.drive.domain.file.ContentGateway;
import com.callv2.drive.domain.file.FileGateway;

@Configuration
public class FileUseCaseConfig {

    private final FileGateway fileGateway;
    private final ContentGateway contentGateway;

    public FileUseCaseConfig(
            final FileGateway fileGateway,
            final ContentGateway contentGateway) {
        this.fileGateway = fileGateway;
        this.contentGateway = contentGateway;
    }

    @Bean
    CreateFileUseCase createFileUseCase() {
        return new DefaultCreateFileUseCase(fileGateway, contentGateway);
    }

}
