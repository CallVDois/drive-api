package com.callv2.drive.infrastructure.file;

import org.springframework.stereotype.Component;

import com.callv2.drive.domain.file.Content;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileContentGateway;
import com.callv2.drive.infrastructure.storage.StorageService;

@Component
public class DefaultFileContentGateway implements FileContentGateway {

    private final StorageService storageService;

    public DefaultFileContentGateway(final StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public void store(final File file, final Content content) {
        storageService.store(file.getContentLocation(), content);
    }

    @Override
    public Content load(final File file) {
        return storageService.load(file.getContentLocation());
    }

}
