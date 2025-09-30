package com.callv2.drive.infrastructure.configuration.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.drive.application.folder.create.CreateFolderUseCase;
import com.callv2.drive.application.folder.create.DefaultCreateFolderUseCase;
import com.callv2.drive.application.folder.delete.DefaultDeleteFolderUseCase;
import com.callv2.drive.application.folder.delete.DeleteFolderUseCase;
import com.callv2.drive.application.folder.move.DefaultMoveFolderUseCase;
import com.callv2.drive.application.folder.move.MoveFolderUseCase;
import com.callv2.drive.application.folder.retrieve.get.DefaultGetFolderUseCase;
import com.callv2.drive.application.folder.retrieve.get.GetFolderUseCase;
import com.callv2.drive.application.folder.retrieve.get.root.DefaultGetRootFolderUseCase;
import com.callv2.drive.application.folder.retrieve.get.root.GetRootFolderUseCase;
import com.callv2.drive.application.folder.retrieve.list.DefaultListFoldersUseCase;
import com.callv2.drive.application.folder.retrieve.list.ListFoldersUseCase;
import com.callv2.drive.application.folder.update.name.DefaultUpdateFolderNameUseCase;
import com.callv2.drive.application.folder.update.name.UpdateFolderNameUseCase;
import com.callv2.drive.domain.access.AclGateway;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.member.MemberGateway;

@Configuration
public class FolderUseCaseConfig {

    private final AclGateway aclGateway;
    private final FolderGateway folderGateway;
    private final FileGateway fileGateway;
    private final MemberGateway memberGateway;
    private final EventDispatcher eventDispatcher;

    public FolderUseCaseConfig(
            final AclGateway aclGateway,
            final FolderGateway folderGateway,
            final FileGateway fileGateway,
            final MemberGateway memberGateway,
            final EventDispatcher eventDispatcher) {
        this.aclGateway = aclGateway;
        this.folderGateway = folderGateway;
        this.fileGateway = fileGateway;
        this.memberGateway = memberGateway;
        this.eventDispatcher = eventDispatcher;
    }

    @Bean
    GetRootFolderUseCase getRootFolderUseCase() {
        return new DefaultGetRootFolderUseCase(eventDispatcher, aclGateway, memberGateway, folderGateway, fileGateway);
    }

    @Bean
    CreateFolderUseCase createFolderUseCase() {
        return new DefaultCreateFolderUseCase(memberGateway, folderGateway);
    }

    @Bean
    GetFolderUseCase getFolderUseCase() {
        return new DefaultGetFolderUseCase(folderGateway, fileGateway);
    }

    @Bean
    MoveFolderUseCase moveFolderUseCase() {
        return new DefaultMoveFolderUseCase(folderGateway);
    }

    @Bean
    ListFoldersUseCase listFoldersUseCase() {
        return new DefaultListFoldersUseCase(folderGateway);
    }

    @Bean
    UpdateFolderNameUseCase updateFolderNameUseCase() {
        return new DefaultUpdateFolderNameUseCase(folderGateway);
    }

    @Bean
    DeleteFolderUseCase deleteFolderUseCase() {
        return new DefaultDeleteFolderUseCase(folderGateway, fileGateway, eventDispatcher);
    }

}
