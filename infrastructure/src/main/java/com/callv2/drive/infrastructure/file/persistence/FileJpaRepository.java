package com.callv2.drive.infrastructure.file.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileJpaRepository extends JpaRepository<FileJpaEntity, UUID> {

    List<FileJpaEntity> findByFolderId(UUID folderId);

}
