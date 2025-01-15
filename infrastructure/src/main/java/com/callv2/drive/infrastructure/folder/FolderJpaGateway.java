package com.callv2.drive.infrastructure.folder;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void updateAll(List<Folder> folders) {
        this.folderRepository.saveAll(folders.stream().map(FolderJpaEntity::fromDomain).toList());
    }

    @Override
    public Optional<Folder> findById(FolderID id) {
        return this.folderRepository.findById(id.getValue()).map(FolderJpaEntity::toDomain);
    }

    private Folder save(Folder folder) {
        return this.folderRepository.save(FolderJpaEntity.fromDomain(folder)).toDomain();
    }

}
