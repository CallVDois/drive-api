package com.callv2.drive.infrastructure.folder.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface FolderJpaRepository extends
        JpaRepository<FolderJpaEntity, UUID>,
        JpaSpecificationExecutor<FolderJpaEntity> {

    Optional<FolderJpaEntity> findByRootFolderTrueAndOwnerId(UUID ownerId);

    List<FolderJpaEntity> findAllByParentFolderId(UUID parentFolderId);

    // @NonNull
    // Page<FolderJpaEntity> findAll(@Nullable Specification<FolderJpaEntity> whereClause, @NonNull Pageable page);

}
