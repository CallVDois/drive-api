package com.callv2.drive.infrastructure.access.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileAclRepository extends JpaRepository<FileAclJpaEntity, FileAclID> {

}
