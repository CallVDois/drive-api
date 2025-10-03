package com.callv2.drive.infrastructure.file;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.SearchQuery;
import com.callv2.drive.infrastructure.access.persistence.FileAclJpaEntity;
import com.callv2.drive.infrastructure.file.persistence.FileJpaEntity;
import com.callv2.drive.infrastructure.file.persistence.FileJpaRepository;
import com.callv2.drive.infrastructure.filter.FilterService;
import com.callv2.drive.infrastructure.filter.adapter.QueryAdapter;

import jakarta.persistence.criteria.Root;

@Component
public class FileJPAGateway implements FileGateway {

    private final FilterService filterService;
    private final FileJpaRepository fileRepository;

    public FileJPAGateway(
            final FileJpaRepository fileRepository,
            final FilterService specificationFilterService) {
        this.fileRepository = fileRepository;
        this.filterService = specificationFilterService;
    }

    @Override
    public File create(File file) {
        return save(file);
    }

    @Override
    public File update(final File file) {
        return save(file);
    }

    @Override
    public Optional<File> findByIdWithMemberAccess(final FileID id, final MemberID memberId) {

        return fileRepository
                .findOne(fileByIdSpecification(id.getValue())
                        .and(fileAclSpecification(memberId.getValue())))
                .map(FileJpaEntity::toDomain);

    }

    @Override
    public List<File> findAllActiveByFolder(FolderID folderId) {
        return this.fileRepository
                .findByFolderIdAndIsDeletedFalse(folderId.getValue())
                .stream()
                .map(FileJpaEntity::toDomain)
                .toList();
    }

    @Override
    public Page<File> findAllWithMemberAccess(final SearchQuery searchQuery, final MemberID memberId) {

        final var page = QueryAdapter.of(searchQuery.pagination());

        final Specification<FileJpaEntity> specification = fileAclSpecification(memberId.getValue())
                .and(filterService.build(
                        FileJpaEntity.class,
                        searchQuery.filterMethod(),
                        searchQuery.filters()));

        final org.springframework.data.domain.Page<FileJpaEntity> pageResult = this.fileRepository
                .findAll(specification, page);

        return new Page<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalPages(),
                pageResult.getTotalElements(),
                pageResult.map(FileJpaEntity::toDomain).toList());

    }

    @Override
    public List<File> findByOwner(MemberID ownerId) {
        return this.fileRepository
                .findByOwnerId(ownerId.getValue())
                .stream()
                .map(FileJpaEntity::toDomain)
                .toList();
    }

    @Override
    public void deleteById(final FileID id) {
        this.fileRepository.deleteById(id.getValue());
    }

    @Override
    public Long sumAllContentSize() {
        return this.fileRepository.sumAllContentSize();
    }

    private File save(File file) {
        this.fileRepository.save(FileJpaEntity.from(file));
        return file;
    }

    private static Specification<FileJpaEntity> fileByIdSpecification(final UUID fileId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), fileId));
        };
    }

    private static Specification<FileJpaEntity> fileAclSpecification(final UUID actorId) {
        return (root, query, criteriaBuilder) -> {

            if (query == null)
                return criteriaBuilder.conjunction();

            final Root<FileAclJpaEntity> aclRoot = query.from(FileAclJpaEntity.class);

            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("id"), aclRoot.get("id").get("fileId")),
                    criteriaBuilder.equal(aclRoot.get("id").get("memberId"), actorId));

        };
    }

}
