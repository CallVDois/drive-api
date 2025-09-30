package com.callv2.drive.infrastructure.access;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.callv2.drive.domain.access.Acl;
import com.callv2.drive.domain.access.AclGateway;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.infrastructure.access.persistence.AclJpaEntity;
import com.callv2.drive.infrastructure.access.persistence.AclJpaRepository;
import com.callv2.drive.infrastructure.access.persistence.FolderAclJpaEntity;
import com.callv2.drive.infrastructure.access.persistence.FolderAclRepository;

@Component
public class AclJpaGateway implements AclGateway {

    private final AclJpaRepository aclJpaRepository;

    private final FolderAclRepository folderAclRepository;

    public AclJpaGateway(
            final AclJpaRepository aclJpaRepository,
            final FolderAclRepository folderAclRepository) {
        this.aclJpaRepository = aclJpaRepository;
        this.folderAclRepository = folderAclRepository;
    }

    @Override
    public Acl create(final Acl acl) {

        switch (acl.getResource().type()) {
            case FOLDER -> saveFolderAcl(acl);
            case FILE -> {
            }
        }

        return aclJpaRepository.save(AclJpaEntity.fromDomain(acl)).toDomain();
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

    private void saveFolderAcl(final Acl acl) {
        final Resource<FolderID> resource = Resource
                .folder(FolderID.fromStringValue(acl.getResource().id().getStringValue()));

        Stream
                .concat(acl.getDirectEntries().stream(), acl.getInheritedEntries().stream())
                .collect(Collectors.toSet())
                .forEach(entry -> folderAclRepository.save(FolderAclJpaEntity.from(resource, entry)));
    }

}
