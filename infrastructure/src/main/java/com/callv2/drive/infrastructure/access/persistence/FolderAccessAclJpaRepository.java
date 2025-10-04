package com.callv2.drive.infrastructure.access.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderAccessAclJpaRepository extends JpaRepository<FolderAccessAclJpaEntity, FolderAclID> {

}
