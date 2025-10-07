package com.callv2.drive.infrastructure.access;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.callv2.drive.domain.access.AccessPermission;
import com.callv2.drive.domain.access.Acl;
import com.callv2.drive.domain.access.AclGateway;
import com.callv2.drive.domain.access.Entry;
import com.callv2.drive.domain.access.Permission;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.infrastructure.access.persistence.AclJpaEntity;
import com.callv2.drive.infrastructure.access.persistence.AclJpaRepository;
import com.callv2.drive.infrastructure.access.persistence.EntryJpaEntity;
import com.callv2.drive.infrastructure.access.persistence.EntryJpaRepository;
import com.callv2.drive.infrastructure.access.persistence.FileAccessAclJpaEntity;
import com.callv2.drive.infrastructure.access.persistence.FileAccessAclJpaRepository;
import com.callv2.drive.infrastructure.access.persistence.FolderAccessAclJpaEntity;
import com.callv2.drive.infrastructure.access.persistence.FolderAccessAclJpaRepository;

@Component
public class AclJpaGateway implements AclGateway {

    private final AclJpaRepository aclJpaRepository;
    private final EntryJpaRepository entryJpaRepository;
    private final FolderAccessAclJpaRepository folderAccessAclJpaRepository;
    private final FileAccessAclJpaRepository fileAccessAclJpaRepository;

    public AclJpaGateway(
            final AclJpaRepository aclJpaRepository,
            final EntryJpaRepository entryJpaRepository,
            final FolderAccessAclJpaRepository folderAccessAclJpaRepository,
            final FileAccessAclJpaRepository fileAccessAclJpaRepository) {
        this.aclJpaRepository = aclJpaRepository;
        this.entryJpaRepository = entryJpaRepository;
        this.folderAccessAclJpaRepository = folderAccessAclJpaRepository;
        this.fileAccessAclJpaRepository = fileAccessAclJpaRepository;
    }

    @Transactional
    @Override
    public Acl create(final Acl acl) {
        return save(acl);
    }

    @Transactional
    @Override
    public Acl update(Acl acl) {
        return save(acl);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Acl> findByResource(Resource<?> resource) {

        return aclJpaRepository
                .findOneByResourceIdAndResourceType(resource.id().getStringValue(), resource.type())
                .map(this::mapToDomain);

    }

    private Acl save(final Acl acl) {

        switch (acl.getResource().type()) {
            case FOLDER -> saveFolderAccessAcl(acl);
            case FILE -> saveFileAccessAcl(acl);
        }

        final AclJpaEntity aclJpa = aclJpaRepository.save(AclJpaEntity.fromDomain(acl));
 
        this.entryJpaRepository.saveAll(Stream.concat(
                acl.getDirectEntries()
                        .stream()
                        .map(entry -> EntryJpaEntity.fromDomain(aclJpa, entry, EntryJpaEntity.Type.DIRECT)),
                acl.getInheritedEntries()
                        .stream()
                        .map(entry -> EntryJpaEntity.fromDomain(aclJpa, entry, EntryJpaEntity.Type.INHERITED)))
                .toList());

        return acl;

    }

    private Acl mapToDomain(final AclJpaEntity aclJpa) {

        final Set<Entry<?>> directEntries = entryJpaRepository
                .findAllByAclIdAndType(aclJpa.getId(), EntryJpaEntity.Type.DIRECT)
                .stream()
                .map(entryJpa -> entryJpa.toDomain())
                .collect(Collectors.toSet());

        final Set<Entry<?>> inheritedEntries = entryJpaRepository
                .findAllByAclIdAndType(aclJpa.getId(), EntryJpaEntity.Type.INHERITED)
                .stream()
                .map(entryJpa -> entryJpa.toDomain())
                .collect(Collectors.toSet());

        return aclJpa.toDomain(directEntries, inheritedEntries);

    }

    private void saveFolderAccessAcl(final Acl acl) {

        final var folderAccessAcls = filterAccessEntries(acl)
                .map(entry -> FolderAccessAclJpaEntity.from(
                        acl.getResource().folder(),
                        entry.getKey(),
                        entry.getValue()))
                .toList();

        this.folderAccessAclJpaRepository.saveAll(folderAccessAcls);

    }

    private void saveFileAccessAcl(final Acl acl) {

        final var fileAccessAcls = filterAccessEntries(acl)
                .map(entry -> FileAccessAclJpaEntity.from(
                        acl.getResource().file(),
                        entry.getKey(),
                        entry.getValue()))
                .toList();

        this.fileAccessAclJpaRepository.saveAll(fileAccessAcls);

    }

    private static Stream<Map.Entry<MemberID, AccessPermission>> filterAccessEntries(final Acl acl) {

        return Stream
                .concat(acl.getDirectEntries().stream(), acl.getInheritedEntries().stream())
                .filter(entry -> Permission.Type.ACCESS.equals(entry.getPermission().type()))
                .collect(Collectors.groupingBy(Entry::getMember,
                        Collectors.mapping(
                                entry -> (AccessPermission) entry.getPermission(),
                                Collectors.minBy(Comparator.comparing(AccessPermission::getLevel)))))
                .entrySet()
                .stream()
                .filter(mapEntry -> mapEntry.getValue().isPresent())
                .map(mapEntry -> Map.entry(mapEntry.getKey(), mapEntry.getValue().get()));

    }

}
