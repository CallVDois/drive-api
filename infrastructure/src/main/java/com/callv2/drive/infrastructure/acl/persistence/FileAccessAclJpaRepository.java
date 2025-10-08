package com.callv2.drive.infrastructure.acl.persistence;

import java.util.Collection;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileAccessAclJpaRepository extends JpaRepository<FileAccessAclJpaEntity, FileAclID> {

    @Modifying
    @Query("""
                delete from FileAccessAcl fa
                where fa.id.fileId = :fileId
                and (:ids is null or fa.id not in :ids)
            """)
    void deleteAllByIdFileIdAndIdNotIn(
            @Param("fileId") UUID fileId,
            @Param("ids") Collection<FileAclID> ids);

}
