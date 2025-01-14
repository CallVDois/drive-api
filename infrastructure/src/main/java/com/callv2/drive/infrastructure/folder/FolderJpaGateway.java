package com.callv2.drive.infrastructure.folder;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.infrastructure.folder.persistence.FolderJpaEntity;
import com.callv2.drive.infrastructure.folder.persistence.FolderJpaRepository;

@Component
public class FolderJpaGateway implements FolderGateway {

    private FolderJpaRepository folderRepository;

    public FolderJpaGateway(final FolderJpaRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    @Override
    public Optional<Folder> findRoot() {
        return this.folderRepository.findByRootFolderTrue().map(FolderJpaEntity::toDomain);
    }

    @Override
    public Folder create(Folder folder) {
        return save(folder);
    }

    @Override
    public Folder update(Folder folder) {
        return save(folder);
    }

    @Override
    public Optional<Folder> findById(FolderID id) {
        return this.folderRepository.findById(id.getValue()).map(FolderJpaEntity::toDomain);
    }

    private Folder save(Folder folder) {
        return this.folderRepository.save(FolderJpaEntity.fromDomain(folder)).toDomain();
    }

}
