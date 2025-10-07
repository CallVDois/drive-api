package com.callv2.drive.infrastructure.acl.persistence;

import java.util.Collection;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface FolderAccessAclJpaRepository extends JpaRepository<FolderAccessAclJpaEntity, FolderAclID> {

    @Modifying
    Integer deleteAllByIdFolderIdAndIdNotIn(UUID folderId, Collection<FolderAclID> ids);

}
