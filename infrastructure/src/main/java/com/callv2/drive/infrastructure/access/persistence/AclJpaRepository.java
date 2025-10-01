package com.callv2.drive.infrastructure.access.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AclJpaRepository extends JpaRepository<AclJpaEntity, UUID> {

}
