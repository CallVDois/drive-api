package com.callv2.drive.infrastructure.access.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryJpaRepository extends JpaRepository<EntryJpaEntity, UUID> {

    List<EntryJpaEntity> findAllByAclIdAndType(UUID aclId, EntryJpaEntity.Type type);

}
