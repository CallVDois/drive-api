package com.callv2.drive.infrastructure.file;

import org.springframework.stereotype.Component;

import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.infrastructure.file.persistence.FileJpaEntity;
import com.callv2.drive.infrastructure.file.persistence.FileJpaRepository;

@Component
public class FileJPAGateway implements FileGateway {

    private final FileJpaRepository fileRepository;

    public FileJPAGateway(final FileJpaRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public File create(File file) {
        return fileRepository.save(FileJpaEntity.fromDomain(file)).toDomain();
    }

}
