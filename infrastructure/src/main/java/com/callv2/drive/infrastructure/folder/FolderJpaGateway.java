package com.callv2.drive.infrastructure.folder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.folder.FolderSharing;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.SearchQuery;
import com.callv2.drive.infrastructure.access.persistence.FolderAccessAclJpaEntity;
import com.callv2.drive.infrastructure.filter.FilterService;
import com.callv2.drive.infrastructure.filter.adapter.QueryAdapter;
import com.callv2.drive.infrastructure.folder.persistence.FolderJpaEntity;
import com.callv2.drive.infrastructure.folder.persistence.FolderJpaRepository;
import com.callv2.drive.infrastructure.folder.persistence.FolderSharingJpaEntity;
import com.callv2.drive.infrastructure.folder.persistence.FolderSharingJpaRepository;

import jakarta.persistence.criteria.Root;

@Component
public class FolderJpaGateway implements FolderGateway {

    private final FilterService filterService;
    private final FolderJpaRepository folderRepository;
    private final FolderSharingJpaRepository folderSharingRepository;

    public FolderJpaGateway(
            final FilterService filterService,
            final FolderJpaRepository folderRepository,
            final FolderSharingJpaRepository folderSharingRepository) {
        this.filterService = filterService;
        this.folderRepository = folderRepository;
        this.folderSharingRepository = folderSharingRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Folder> findMemberRootFolder(final MemberID owner) {
        return this.folderRepository.findByRootFolderTrueAndOwnerId(owner.getValue()).map(this::mapToDomain);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Folder> findDefaultMemberSharedInbox(final MemberID owner) {
        return this.folderRepository
                .findByDefaultSharedInboxTrueAndOwnerId(owner.getValue())
                .map(this::mapToDomain);
    }

    @Override
    public Set<Folder> findByParentFolderId(final FolderID parentFolderId) {
        return mapToDomain(this.folderRepository.findAll(findByParentFolderIdSpecification(parentFolderId.getValue())))
                .stream()
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Folder> findByParentFolderIdWithMemberAccess(FolderID parentFolderId, final MemberID actorId) {
        return mapToDomain(this.folderRepository.findAll(
                findByParentFolderIdSpecification(parentFolderId.getValue())
                        .and(folderAclSpecification(actorId.getValue()))))
                .stream()
                .collect(Collectors.toSet());
    }

    @Override
    public Folder create(Folder folder) {
        return save(folder);
    }

    @Override
    public Folder update(Folder folder) {
        return save(folder);
    }

    @Transactional
    @Override
    public void updateAll(List<Folder> folders) {
        this.folderRepository.saveAll(folders.stream().map(FolderJpaEntity::fromDomain).toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Folder> findByIdWithMemberAccess(final FolderID id, final MemberID actorId) {

        final var specification = folderByIdSpecification(id.getValue())
                .and(folderByIdSpecification(id.getValue()))
                .and(folderAclSpecification(actorId.getValue()));

        return this.folderRepository
                .findOne(specification)
                .map(this::mapToDomain);

    }

    @Transactional(readOnly = true)
    @Override
    public Page<Folder> findAllWithMemberAccess(final SearchQuery searchQuery, final MemberID actorId) {
        final var page = QueryAdapter.of(searchQuery.pagination());

        final Specification<FolderJpaEntity> specification = folderAclSpecification(actorId.getValue())
                .and(filterService.build(
                        FolderJpaEntity.class,
                        searchQuery.filterMethod(),
                        searchQuery.filters()));

        final org.springframework.data.domain.Page<FolderJpaEntity> pageResult = this.folderRepository.findAll(
                specification,
                page);

        return new Page<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalPages(),
                pageResult.getTotalElements(),
                mapToDomain(pageResult.toList()));
    }

    @Override
    public void deleteById(FolderID id) {
        this.folderRepository.deleteById(id.getValue());
    }

    private Folder mapToDomain(final FolderJpaEntity entity) {
        final List<FolderSharingJpaEntity> sharings = this.folderSharingRepository.findAllByFolderId(entity.getId());
        return entity.toDomain(
                sharings
                        .stream()
                        .map(FolderSharingJpaEntity::toDomain)
                        .collect(Collectors.toSet()));
    }

    private List<Folder> mapToDomain(final Collection<FolderJpaEntity> folderJpaEntities) {

        final Map<UUID, Set<FolderSharing>> sharingsByFolderId = this.folderSharingRepository
                .findAllByFolderIdIn(folderJpaEntities.stream().map(FolderJpaEntity::getId).toList())
                .stream()
                .collect(
                        Collectors.groupingBy(fs -> fs.getFolder().getId(),
                                Collectors.mapping(folderShareJpa -> folderShareJpa.toDomain(), Collectors.toSet())));

        return folderJpaEntities
                .stream()
                .map(folder -> folder.toDomain(sharingsByFolderId.get(folder.getId()))).toList();
    }

    private Folder save(final Folder folder) {

        final FolderJpaEntity folderJpa = this.folderRepository.save(FolderJpaEntity.fromDomain(folder));

        this.folderSharingRepository
                .saveAll(
                        folder
                                .getSharings()
                                .stream()
                                .map(folderSharing -> FolderSharingJpaEntity.from(folderJpa, folderSharing))
                                .toList());

        return folder;
    }

    private static Specification<FolderJpaEntity> folderAclSpecification(final UUID actorId) {
        return (root, query, criteriaBuilder) -> {

            if (query == null)
                return criteriaBuilder.conjunction();

            final Root<FolderAccessAclJpaEntity> aclRoot = query.from(FolderAccessAclJpaEntity.class);

            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("id"), aclRoot.get("id").get("folderId")),
                    criteriaBuilder.equal(aclRoot.get("id").get("memberId"), actorId));

        };
    }

    private static Specification<FolderJpaEntity> folderByIdSpecification(final UUID folderId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), folderId));
    }

    private static Specification<FolderJpaEntity> findByParentFolderIdSpecification(final UUID parentFolderId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("parentFolderId"), parentFolderId));
    }

}
