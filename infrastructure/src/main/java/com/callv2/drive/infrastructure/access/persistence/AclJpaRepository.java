package com.callv2.drive.infrastructure.access.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.callv2.drive.domain.access.ResourceType;

public interface AclJpaRepository extends JpaRepository<AclJpaEntity, UUID> {

    Optional<AclJpaEntity> findOneByResourceIdAndResourceType(String resourceId, ResourceType resourceType);

}
