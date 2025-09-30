package com.callv2.drive.infrastructure.folder.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FolderJpaRepository extends
        JpaRepository<FolderJpaEntity, UUID>,
        JpaSpecificationExecutor<FolderJpaEntity> {

    Optional<FolderJpaEntity> findByRootFolderTrueAndOwnerId(UUID ownerId);

    List<FolderJpaEntity> findAllByParentFolderId(UUID parentFolderId);

}
