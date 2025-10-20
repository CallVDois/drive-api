package com.callv2.drive.infrastructure.configuration.application.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.drive.application.folder.service.FolderProvisioningApplicationService;
import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.folder.FolderGateway;

@Configuration
public class FolderApplicationServiceConfig {

    private final EventDispatcher eventDispatcher;
    private final FolderGateway folderGateway;
    private final AclGateway aclGateway;

    public FolderApplicationServiceConfig(
            final EventDispatcher eventDispatcher,
            final FolderGateway folderGateway,
            final AclGateway aclGateway) {
        this.eventDispatcher = eventDispatcher;
        this.folderGateway = folderGateway;
        this.aclGateway = aclGateway;
    }

    @Bean
    FolderProvisioningApplicationService folderProvisioningApplicationService() {
        return new FolderProvisioningApplicationService(
                eventDispatcher,
                folderGateway,
                aclGateway);
    }

}
