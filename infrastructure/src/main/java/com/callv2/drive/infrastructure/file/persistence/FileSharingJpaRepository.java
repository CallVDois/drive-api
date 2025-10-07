package com.callv2.drive.infrastructure.file.persistence;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface FileSharingJpaRepository extends JpaRepository<FileSharingJpaEntity, UUID> {

    @Modifying
    Integer deleteAllByFileIdAndIdNotIn(UUID fileId, Collection<UUID> ids);

    List<FileSharingJpaEntity> findAllByFileId(UUID fileId);

    List<FileSharingJpaEntity> findAllByFileIdIn(Collection<UUID> fileIds);

}
