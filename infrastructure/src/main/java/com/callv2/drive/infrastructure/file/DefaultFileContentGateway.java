package com.callv2.drive.infrastructure.file;

import java.io.InputStream;

import org.springframework.stereotype.Component;

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
    public void store(final File file, final InputStream inputStream) {
        storageService.store(file.getContentLocation(), inputStream);
    }

    @Override
    public InputStream load(final File file) {
        return storageService.load(file.getContentLocation());
    }

}
