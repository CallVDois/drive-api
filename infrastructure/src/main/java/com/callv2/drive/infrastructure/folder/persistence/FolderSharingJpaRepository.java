package com.callv2.drive.infrastructure.folder.persistence;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FolderSharingJpaRepository extends JpaRepository<FolderSharingJpaEntity, UUID> {

    @Modifying
    @Query("""
                delete from FolderSharing fs
                where fs.folder.id = :folderId
                and (:ids is null or fs.id not in :ids)
            """)
    void deleteAllByFolderIdAndIdNotIn(@Param("folderId") UUID folderId, @Param("ids") Collection<UUID> ids);

    List<FolderSharingJpaEntity> findAllByFolderId(UUID folderId);

    List<FolderSharingJpaEntity> findAllByFolderIdIn(Collection<UUID> folderIds);

}
