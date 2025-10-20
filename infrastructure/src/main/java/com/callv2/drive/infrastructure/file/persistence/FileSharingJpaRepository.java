package com.callv2.drive.infrastructure.file.persistence;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileSharingJpaRepository extends JpaRepository<FileSharingJpaEntity, UUID> {

    @Modifying
    @Query("""
                delete from FileSharing f
                where f.file.id = :fileId
                and (:ids is null or f.id not in :ids)
            """)
    void deleteAllByFileIdAndIdNotIn(@Param("fileId") UUID fileId, @Param("ids") Collection<UUID> ids);

    List<FileSharingJpaEntity> findAllByFileId(UUID fileId);

    List<FileSharingJpaEntity> findAllByFileIdIn(Collection<UUID> fileIds);

}
