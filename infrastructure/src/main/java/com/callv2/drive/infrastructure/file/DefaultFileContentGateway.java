package com.callv2.drive.infrastructure.file;

import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.callv2.drive.domain.file.FileContentGateway;
import com.callv2.drive.infrastructure.storage.StorageService;

@Component
public class DefaultFileContentGateway implements FileContentGateway {

    private final StorageService storageService;

    public DefaultFileContentGateway(final StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public String store(final String contentName, final InputStream inputStream) {
        return storageService.store(contentName, inputStream);
    }

    @Override
    public void delete(String contentLocation) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
