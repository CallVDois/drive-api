package com.callv2.drive.infrastructure.file;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.file.FileSharing;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.SearchQuery;
import com.callv2.drive.infrastructure.access.persistence.FileAccessAclJpaEntity;
import com.callv2.drive.infrastructure.file.persistence.FileJpaEntity;
import com.callv2.drive.infrastructure.file.persistence.FileJpaRepository;
import com.callv2.drive.infrastructure.file.persistence.FileSharingJpaEntity;
import com.callv2.drive.infrastructure.file.persistence.FileSharingJpaRepository;
import com.callv2.drive.infrastructure.filter.FilterService;
import com.callv2.drive.infrastructure.filter.adapter.QueryAdapter;

import jakarta.persistence.criteria.Root;

@Component
public class FileJPAGateway implements FileGateway {

    private final FilterService filterService;
    private final FileJpaRepository fileRepository;
    private final FileSharingJpaRepository fileSharingRepository;

    public FileJPAGateway(
            final FilterService specificationFilterService,
            final FileJpaRepository fileRepository,
            final FileSharingJpaRepository fileSharingRepository) {
        this.fileRepository = fileRepository;
        this.fileSharingRepository = fileSharingRepository;
        this.filterService = specificationFilterService;
    }

    @Transactional
    @Override
    public File create(final File file) {
        return save(file);
    }

    @Transactional
    @Override
    public File update(final File file) {
        return save(file);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<File> findByIdWithMemberAccess(final FileID id, final MemberID memberId) {

        return fileRepository
                .findOne(fileByIdSpecification(id.getValue()).and(fileAclSpecification(memberId.getValue())))
                .map(this::mapToDomain);

    }

    @Transactional(readOnly = true)
    @Override
    public List<File> findAllActiveByFolder(final FolderID folderId) {
        return mapToDomain(this.fileRepository.findByFolderIdAndIsDeletedFalse(folderId.getValue()));
    }

    @Transactional(readOnly = true)
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
                mapToDomain(pageResult.toList()));

    }

    @Transactional(readOnly = true)
    @Override
    public List<File> findByOwner(final MemberID ownerId) {
        return mapToDomain(this.fileRepository.findByOwnerId(ownerId.getValue()));
    }

    @Override
    public void deleteById(final FileID id) {
        this.fileRepository.deleteById(id.getValue());
    }

    @Override
    public Long sumAllContentSize() {
        return this.fileRepository.sumAllContentSize();
    }

    private File save(final File file) {

        final FileJpaEntity fileJpa = this.fileRepository.save(FileJpaEntity.fromDomain(file));

        final var fileSharings = file
                .getSharings()
                .stream()
                .map(fileSharing -> FileSharingJpaEntity.from(fileJpa, fileSharing))
                .toList();

        final var currentSharingIds = fileSharings.stream().map(FileSharingJpaEntity::getId).toList();

        this.fileSharingRepository.deleteAllByFileIdAndIdNotIn(fileJpa.getId(), currentSharingIds);

        this.fileSharingRepository
                .saveAll(
                        file
                                .getSharings()
                                .stream()
                                .map(fileSharing -> FileSharingJpaEntity.from(fileJpa, fileSharing))
                                .toList());

        return file;
    }

    private File mapToDomain(final FileJpaEntity entity) {
        final List<FileSharingJpaEntity> sharings = this.fileSharingRepository.findAllByFileId(entity.getId());
        return entity.toDomain(
                sharings
                        .stream()
                        .map(FileSharingJpaEntity::toDomain)
                        .collect(Collectors.toSet()));
    }

    private List<File> mapToDomain(final List<FileJpaEntity> fileJpaEntities) {

        final List<UUID> fileIds = fileJpaEntities.stream().map(FileJpaEntity::getId).toList();

        final List<FileSharingJpaEntity> sharingsJpaEntities = this.fileSharingRepository.findAllByFileIdIn(fileIds);
        final Map<UUID, Set<FileSharing>> sharingsByFileId = sharingsJpaEntities.stream()
                .collect(Collectors.groupingBy(fs -> fs.getFile().getId(),
                        Collectors.mapping(fileShareJpa -> fileShareJpa.toDomain(), Collectors.toSet())));

        return fileJpaEntities.stream().map(file -> file.toDomain(sharingsByFileId.get(file.getId()))).toList();
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

            final Root<FileAccessAclJpaEntity> aclRoot = query.from(FileAccessAclJpaEntity.class);

            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("id"), aclRoot.get("id").get("fileId")),
                    criteriaBuilder.equal(aclRoot.get("id").get("memberId"), actorId));

        };
    }

}
