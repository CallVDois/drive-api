package com.callv2.drive.infrastructure.configuration.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.drive.application.folder.create.CreateFolderUseCase;
import com.callv2.drive.application.folder.create.DefaultCreateFolderUseCase;
import com.callv2.drive.application.folder.retrieve.get.DefaultGetFolderUseCase;
import com.callv2.drive.application.folder.retrieve.get.GetFolderUseCase;
import com.callv2.drive.domain.folder.FolderGateway;

@Configuration
public class FolderUseCaseConfig {

    private final FolderGateway folderGateway;

    public FolderUseCaseConfig(final FolderGateway folderGateway) {
        this.folderGateway = folderGateway;
    }

    @Bean
    CreateFolderUseCase createFolderUseCase() {
        return new DefaultCreateFolderUseCase(folderGateway);
    }

    @Bean
    GetFolderUseCase getFolderUseCase() {
        return new DefaultGetFolderUseCase(folderGateway);
    }

}
