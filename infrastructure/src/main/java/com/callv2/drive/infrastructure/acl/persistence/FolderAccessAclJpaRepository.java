package com.callv2.drive.infrastructure.acl.persistence;

import java.util.Collection;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FolderAccessAclJpaRepository extends JpaRepository<FolderAccessAclJpaEntity, FolderAclID> {

    @Modifying
    @Query("""
                delete from FolderAccessAcl fa
                where fa.id.folderId = :folderId
                and (:ids is null or fa.id not in :ids)
            """)
    void deleteAllByIdFolderIdAndIdNotIn(
            @Param("folderId") UUID folderId,
            @Param("ids") Collection<FolderAclID> ids);

}
