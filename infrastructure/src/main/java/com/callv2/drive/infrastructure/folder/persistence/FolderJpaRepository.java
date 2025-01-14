package com.callv2.drive.infrastructure.folder.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderJpaRepository extends JpaRepository<FolderJpaEntity, UUID> {

    Optional<FolderJpaEntity> findByRootFolderTrue();

}
