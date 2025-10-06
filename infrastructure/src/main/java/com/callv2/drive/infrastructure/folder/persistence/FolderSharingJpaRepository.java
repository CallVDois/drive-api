package com.callv2.drive.infrastructure.folder.persistence;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderSharingJpaRepository extends JpaRepository<FolderSharingJpaEntity, UUID> {

    List<FolderSharingJpaEntity> findAllByFolderId(UUID folderId);

    List<FolderSharingJpaEntity> findAllByFolderIdIn(Collection<UUID> folderIds);

}
