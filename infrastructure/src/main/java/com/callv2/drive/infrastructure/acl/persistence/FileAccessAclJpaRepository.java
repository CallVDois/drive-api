package com.callv2.drive.infrastructure.acl.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileAccessAclJpaRepository extends JpaRepository<FileAccessAclJpaEntity, FileAclID> {

}
