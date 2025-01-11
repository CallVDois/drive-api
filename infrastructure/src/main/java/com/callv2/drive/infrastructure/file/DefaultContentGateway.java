package com.callv2.drive.infrastructure.file;

import org.springframework.stereotype.Component;

import com.callv2.drive.domain.file.BinaryContent;
import com.callv2.drive.domain.file.ContentGateway;
import com.callv2.drive.infrastructure.storage.StorageService;

@Component
public class DefaultContentGateway implements ContentGateway {

    private final StorageService storageService;

    public DefaultContentGateway(final StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public void store(BinaryContent content) {
        storageService.store(content.getId().getValue().toString(), content.bytes());
    }

}
