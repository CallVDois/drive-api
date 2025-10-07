package com.callv2.drive.infrastructure.file.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface FileJpaRepository extends
        JpaRepository<FileJpaEntity, UUID>, JpaSpecificationExecutor<FileJpaEntity> {

    List<FileJpaEntity> findByFolderId(UUID folderId);

    List<FileJpaEntity> findByOwnerId(UUID ownerId);

    @Query("select coalesce(sum(f.contentSize), 0) from File f")
    Long sumAllContentSize();

}
