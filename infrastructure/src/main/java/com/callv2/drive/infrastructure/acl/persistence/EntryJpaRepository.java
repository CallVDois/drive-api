package com.callv2.drive.infrastructure.acl.persistence;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EntryJpaRepository extends JpaRepository<EntryJpaEntity, UUID> {

    @Modifying
    @Query("""
                delete from EntryJpa e
                where e.acl.id = :aclId
                and (:ids is null or e.id not in :ids)
            """)
    void deleteAllByAclIdAndIdNotIn(@Param("aclId") UUID aclId, @Param("ids") Collection<UUID> ids);

    List<EntryJpaEntity> findAllByAclIdAndType(UUID aclId, EntryJpaEntity.Type type);

}
