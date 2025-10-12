package com.callv2.drive.infrastructure.configuration.application.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.drive.application.folder.service.FolderProvisioningApplicationService;
import com.callv2.drive.application.folder.usecase.create.CreateFolderUseCase;
import com.callv2.drive.application.folder.usecase.create.DefaultCreateFolderUseCase;
import com.callv2.drive.application.folder.usecase.delete.DefaultDeleteFolderUseCase;
import com.callv2.drive.application.folder.usecase.delete.DeleteFolderUseCase;
import com.callv2.drive.application.folder.usecase.move.DefaultMoveFolderUseCase;
import com.callv2.drive.application.folder.usecase.move.MoveFolderUseCase;
import com.callv2.drive.application.folder.usecase.retrieve.get.DefaultGetFolderUseCase;
import com.callv2.drive.application.folder.usecase.retrieve.get.GetFolderUseCase;
import com.callv2.drive.application.folder.usecase.retrieve.get.root.DefaultGetRootFolderUseCase;
import com.callv2.drive.application.folder.usecase.retrieve.get.root.GetRootFolderUseCase;
import com.callv2.drive.application.folder.usecase.retrieve.list.DefaultListFoldersUseCase;
import com.callv2.drive.application.folder.usecase.retrieve.list.ListFoldersUseCase;
import com.callv2.drive.application.folder.usecase.sharing.create.CreateFolderSharingUseCase;
import com.callv2.drive.application.folder.usecase.sharing.create.DefaultCreateFolderSharingUseCase;
import com.callv2.drive.application.folder.usecase.sharing.retrieve.list.DefaultListFolderSharingUseCase;
import com.callv2.drive.application.folder.usecase.sharing.retrieve.list.ListFolderSharingUseCase;
import com.callv2.drive.application.folder.usecase.update.name.DefaultUpdateFolderNameUseCase;
import com.callv2.drive.application.folder.usecase.update.name.UpdateFolderNameUseCase;
import com.callv2.drive.domain.acl.AclGateway;
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

    private final FolderProvisioningApplicationService folderProvisioningApplicationService;

    public FolderUseCaseConfig(
            final AclGateway aclGateway,
            final FolderGateway folderGateway,
            final FileGateway fileGateway,
            final MemberGateway memberGateway,
            final EventDispatcher eventDispatcher,
            final FolderProvisioningApplicationService folderProvisioningApplicationService) {
        this.aclGateway = aclGateway;
        this.folderGateway = folderGateway;
        this.fileGateway = fileGateway;
        this.memberGateway = memberGateway;
        this.eventDispatcher = eventDispatcher;
        this.folderProvisioningApplicationService = folderProvisioningApplicationService;
    }

    @Bean
    GetRootFolderUseCase getRootFolderUseCase() {
        return new DefaultGetRootFolderUseCase(
                folderGateway,
                fileGateway,
                folderProvisioningApplicationService);
    }

    @Bean
    CreateFolderUseCase createFolderUseCase() {
        return new DefaultCreateFolderUseCase(folderProvisioningApplicationService, memberGateway);
    }

    @Bean
    GetFolderUseCase getFolderUseCase() {
        return new DefaultGetFolderUseCase(folderGateway, fileGateway);
    }

    @Bean
    MoveFolderUseCase moveFolderUseCase() {
        return new DefaultMoveFolderUseCase(aclGateway, folderGateway);
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

    @Bean
    CreateFolderSharingUseCase createFolderSharingUseCase() {
        return new DefaultCreateFolderSharingUseCase(
                eventDispatcher,
                memberGateway,
                aclGateway,
                folderGateway,
                folderProvisioningApplicationService);
    }

    @Bean
    ListFolderSharingUseCase listFolderSharingUseCase() {
        return new DefaultListFolderSharingUseCase(folderGateway, aclGateway);
    }

}
