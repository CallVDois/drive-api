package com.callv2.drive.infrastructure.configuration.application.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.drive.application.acl.usecase.inheritance.recalculate.DefaultRecalculateAclInheritanceUseCase;
import com.callv2.drive.application.acl.usecase.inheritance.recalculate.RecalculateAclInheritanceUseCase;
import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.folder.FolderGateway;

@Configuration
public class AclUseCaseConfig {

    private final EventDispatcher eventDispatcher;
    private final AclGateway aclGateway;
    private final FileGateway fileGateway;
    private final FolderGateway folderGateway;

    public AclUseCaseConfig(
            final EventDispatcher eventDispatcher,
            final AclGateway aclGateway,
            final FileGateway fileGateway,
            final FolderGateway folderGateway) {
        this.eventDispatcher = eventDispatcher;
        this.aclGateway = aclGateway;
        this.fileGateway = fileGateway;
        this.folderGateway = folderGateway;
    }

    @Bean
    RecalculateAclInheritanceUseCase recalculateAclInheritanceUseCase() {
        return new DefaultRecalculateAclInheritanceUseCase(
                eventDispatcher,
                aclGateway,
                fileGateway,
                folderGateway);
    }

}
