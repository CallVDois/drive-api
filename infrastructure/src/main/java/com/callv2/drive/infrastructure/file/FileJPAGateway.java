package com.callv2.drive.infrastructure.file;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.folder.FolderID;
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
        return fileRepository.save(FileJpaEntity.from(file)).toDomain();
    }

    @Override
    public Optional<File> findById(FileID id) {
        return fileRepository.findById(id.getValue()).map(FileJpaEntity::toDomain);
    }

    @Override
    public List<File> findByFolder(FolderID folderId) {
        return this.fileRepository
                .findByFolderId(folderId.getValue())
                .stream()
                .map(FileJpaEntity::toDomain)
                .toList();
    }

}
