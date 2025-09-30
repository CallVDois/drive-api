package com.callv2.drive.infrastructure.folder;

import java.util.List;
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
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.SearchQuery;
import com.callv2.drive.infrastructure.access.persistence.FolderAclJpaEntity;
import com.callv2.drive.infrastructure.filter.FilterService;
import com.callv2.drive.infrastructure.filter.adapter.QueryAdapter;
import com.callv2.drive.infrastructure.folder.persistence.FolderJpaEntity;
import com.callv2.drive.infrastructure.folder.persistence.FolderJpaRepository;

import jakarta.persistence.criteria.Root;

@Component
public class FolderJpaGateway implements FolderGateway {

    private final FilterService filterService;
    private final FolderJpaRepository folderRepository;

    public FolderJpaGateway(
            final FilterService filterService,
            final FolderJpaRepository folderRepository) {
        this.filterService = filterService;
        this.folderRepository = folderRepository;
    }

    @Override
    public Optional<Folder> findMemberRootFolder(final MemberID owner) {
        return this.folderRepository.findByRootFolderTrueAndOwnerId(owner.getValue()).map(FolderJpaEntity::toDomain);
    }

    @Override
    public Set<Folder> findByParentFolderIdWithMemberAccess(FolderID parentFolderId, final MemberID actorId) {
        return this.folderRepository
                .findAll(
                        findByParentFolderIdSpecification(parentFolderId.getValue())
                                .and(folderAclSpecification(actorId.getValue())))
                .stream()
                .map(FolderJpaEntity::toDomain)
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

    @Override
    @Transactional
    public void updateAll(List<Folder> folders) {
        this.folderRepository.saveAll(folders.stream().map(FolderJpaEntity::fromDomain).toList());
    }

    @Override
    public Optional<Folder> findByIdWithMemberAccess(final FolderID id, final MemberID actorId) {

        final var specification = folderByIdSpecification(id.getValue())
                .and(folderAclSpecification(actorId.getValue()));

        return this.folderRepository
                .findOne(specification)
                .map(FolderJpaEntity::toDomain);

    }

    private Folder save(Folder folder) {
        return this.folderRepository.save(FolderJpaEntity.fromDomain(folder)).toDomain();
    }

    @Override
    public Page<Folder> findAllWithMemberAccess(final SearchQuery searchQuery, final MemberID actorId) {
        final var page = QueryAdapter.of(searchQuery.pagination());

        final Specification<FolderJpaEntity> specification = folderAclSpecification(actorId.getValue())
                .and(filterService.buildSpecification(
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
                pageResult.map(FolderJpaEntity::toDomain).toList());
    }

    @Override
    public void deleteById(FolderID id) {
        this.folderRepository.deleteById(id.getValue());
    }

    private static Specification<FolderJpaEntity> folderAclSpecification(final UUID actorId) {
        return (root, query, criteriaBuilder) -> {

            if (query == null)
                return criteriaBuilder.conjunction();

            final Root<FolderAclJpaEntity> aclRoot = query.from(FolderAclJpaEntity.class);

            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("id"), aclRoot.get("id").get("folderId")),
                    criteriaBuilder.equal(aclRoot.get("id").get("memberId"), actorId));

        };
    }

    private static Specification<FolderJpaEntity> folderByIdSpecification(final UUID folderId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), folderId));
        };
    }

    private static Specification<FolderJpaEntity> findByParentFolderIdSpecification(final UUID parentFolderId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(criteriaBuilder.equal(root.get("parentFolderId"), parentFolderId));
        };
    }

}
