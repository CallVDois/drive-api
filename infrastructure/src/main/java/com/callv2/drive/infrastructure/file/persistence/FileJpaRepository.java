package com.callv2.drive.infrastructure.file.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileJpaRepository extends JpaRepository<FileJpaEntity, UUID> {

    Page<FileJpaEntity> findAll(Specification<FileJpaEntity> whereClause, Pageable page);

    List<FileJpaEntity> findByFolderId(UUID folderId);

    List<FileJpaEntity> findByOwnerId(String ownerId);

}
