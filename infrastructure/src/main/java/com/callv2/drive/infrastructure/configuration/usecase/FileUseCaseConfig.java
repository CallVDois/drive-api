package com.callv2.drive.infrastructure.configuration.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.drive.application.file.content.delete.DefaultlDeleteFileContentUseCase;
import com.callv2.drive.application.file.content.delete.DeleteFileContentUseCase;
import com.callv2.drive.application.file.content.get.DefaultGetFileContentUseCase;
import com.callv2.drive.application.file.content.get.GetFileContentUseCase;
import com.callv2.drive.application.file.create.CreateFileUseCase;
import com.callv2.drive.application.file.create.DefaultCreateFileUseCase;
import com.callv2.drive.application.file.delete.DefaultDeleteFileUseCase;
import com.callv2.drive.application.file.delete.DeleteFileUseCase;
import com.callv2.drive.application.file.retrieve.get.DefaultGetFileUseCase;
import com.callv2.drive.application.file.retrieve.get.GetFileUseCase;
import com.callv2.drive.application.file.retrieve.list.DefaultListFilesUseCase;
import com.callv2.drive.application.file.retrieve.list.ListFilesUseCase;
import com.callv2.drive.application.file.sharing.create.CreateFileSharingUseCase;
import com.callv2.drive.application.file.sharing.create.DefaultCreateFileSharingUseCase;
import com.callv2.drive.application.file.sharing.remove.DefaultRemoveFileSharingUseCase;
import com.callv2.drive.application.file.sharing.remove.RemoveFileSharingUseCase;
import com.callv2.drive.application.file.sharing.retrieve.list.DefaultListFileSharingUseCase;
import com.callv2.drive.application.file.sharing.retrieve.list.ListFileSharingUseCase;
import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.storage.StorageGateway;
import com.callv2.drive.domain.storage.StorageKeyGenerator;

@Configuration
public class FileUseCaseConfig {

    private final AclGateway aclGateway;
    private final MemberGateway memberGateway;
    private final FolderGateway folderGateway;
    private final FileGateway fileGateway;
    private final StorageKeyGenerator storageKeyGenerator;
    private final StorageGateway storageGateway;
    private final EventDispatcher eventDispatcher;

    public FileUseCaseConfig(
            final AclGateway aclGateway,
            final MemberGateway memberGateway,
            final FolderGateway folderGateway,
            final FileGateway fileGateway,
            final StorageKeyGenerator storageKeyGenerator,
            final StorageGateway storageService,
            final EventDispatcher eventDispatcher) {
        this.aclGateway = aclGateway;
        this.memberGateway = memberGateway;
        this.folderGateway = folderGateway;
        this.fileGateway = fileGateway;
        this.storageKeyGenerator = storageKeyGenerator;
        this.storageGateway = storageService;
        this.eventDispatcher = eventDispatcher;
    }

    @Bean
    CreateFileUseCase createFileUseCase() {
        return new DefaultCreateFileUseCase(
                eventDispatcher,
                memberGateway,
                folderGateway,
                fileGateway,
                storageKeyGenerator,
                storageGateway,
                aclGateway);
    }

    @Bean
    DeleteFileUseCase deleteFileUseCase() {
        return new DefaultDeleteFileUseCase(memberGateway, fileGateway, eventDispatcher);
    }

    @Bean
    GetFileUseCase getFileUseCase() {
        return new DefaultGetFileUseCase(fileGateway);
    }

    @Bean
    GetFileContentUseCase getFileContentUseCase() {
        return new DefaultGetFileContentUseCase(fileGateway, storageGateway);
    }

    @Bean
    ListFilesUseCase listFilesUseCase() {
        return new DefaultListFilesUseCase(fileGateway);
    }

    @Bean
    DeleteFileContentUseCase deleteFileContentUseCase() {
        return new DefaultlDeleteFileContentUseCase(fileGateway, storageGateway);
    }

    @Bean
    CreateFileSharingUseCase createFileSharingUseCase() {
        return new DefaultCreateFileSharingUseCase(
                eventDispatcher,
                memberGateway,
                aclGateway,
                fileGateway,
                folderGateway);
    }

    @Bean
    RemoveFileSharingUseCase removeFileSharingUseCase() {
        return new DefaultRemoveFileSharingUseCase(
                eventDispatcher,
                fileGateway,
                aclGateway);
    }

    @Bean
    ListFileSharingUseCase listFileSharingUseCase() {
        return new DefaultListFileSharingUseCase(fileGateway, aclGateway);
    }

}
