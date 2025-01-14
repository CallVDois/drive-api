package com.callv2.drive.infrastructure.folder.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<FolderJpaEntity, UUID> {

}
