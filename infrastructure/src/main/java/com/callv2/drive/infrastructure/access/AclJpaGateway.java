package com.callv2.drive.infrastructure.access;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.callv2.drive.domain.access.Acl;
import com.callv2.drive.domain.access.AclGateway;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.infrastructure.access.persistence.AclJpaEntity;
import com.callv2.drive.infrastructure.access.persistence.AclJpaRepository;
import com.callv2.drive.infrastructure.access.persistence.FileAclJpaEntity;
import com.callv2.drive.infrastructure.access.persistence.FileAclRepository;
import com.callv2.drive.infrastructure.access.persistence.FolderAclJpaEntity;
import com.callv2.drive.infrastructure.access.persistence.FolderAclRepository;

@Component
public class AclJpaGateway implements AclGateway {

    private final AclJpaRepository aclJpaRepository;
    private final FolderAclRepository folderAclRepository;
    private final FileAclRepository fileAclRepository;

    public AclJpaGateway(
            final AclJpaRepository aclJpaRepository,
            final FolderAclRepository folderAclRepository,
            final FileAclRepository fileAclRepository) {
        this.aclJpaRepository = aclJpaRepository;
        this.folderAclRepository = folderAclRepository;
        this.fileAclRepository = fileAclRepository;
    }

    @Override
    public Acl create(final Acl acl) {
        return save(acl);
    }

    @Override
    public Acl update(Acl acl) {
        return save(acl);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Acl> findByResource(Resource<?> resource) {

        return aclJpaRepository.findAll()
                .stream()
                .map(AclJpaEntity::toDomain)
                .filter(acl -> acl.getResource().equals(resource))
                .findFirst();

    }

    private Acl save(final Acl acl) {
        switch (acl.getResource().type()) {
            case FOLDER -> saveFolderAcl(acl);
            case FILE -> saveFileAcl(acl);
        }

        return aclJpaRepository.save(AclJpaEntity.fromDomain(acl)).toDomain();
    }

    private void saveFolderAcl(final Acl acl) {

        final Resource<FolderID> resource = Resource
                .folder(FolderID.fromStringValue(acl.getResource().id().getStringValue()));

        this.folderAclRepository.saveAll(Stream
                .concat(acl.getDirectEntries().stream(), acl.getInheritedEntries().stream())
                .map(entry -> FolderAclJpaEntity.from(resource, entry))
                .collect(Collectors.toSet()));

    }

    private void saveFileAcl(final Acl acl) {

        final Resource<FileID> resource = Resource
                .file(FileID.fromStringValue(acl.getResource().id().getStringValue()));

        this.fileAclRepository.saveAll(Stream
                .concat(acl.getDirectEntries().stream(), acl.getInheritedEntries().stream())
                .map(entry -> FileAclJpaEntity.from(resource, entry))
                .collect(Collectors.toSet()));

    }

}
