package com.callv2.drive.infrastructure.access.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileAccessAclJpaRepository extends JpaRepository<FileAccessAclJpaEntity, FileAclID> {

}
