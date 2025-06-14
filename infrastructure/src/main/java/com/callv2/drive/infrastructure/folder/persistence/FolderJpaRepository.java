package com.callv2.drive.infrastructure.folder.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderJpaRepository extends JpaRepository<FolderJpaEntity, UUID> {

    Optional<FolderJpaEntity> findByRootFolderTrue();

    List<FolderJpaEntity> findAllByParentFolderId(UUID parentFolderId);

    Page<FolderJpaEntity> findAll(Specification<FolderJpaEntity> whereClause, Pageable page);

}
