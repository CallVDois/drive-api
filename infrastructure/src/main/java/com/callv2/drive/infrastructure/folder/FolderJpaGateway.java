package com.callv2.drive.infrastructure.folder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.SearchQuery;
import com.callv2.drive.infrastructure.filter.FilterService;
import com.callv2.drive.infrastructure.filter.adapter.QueryAdapter;
import com.callv2.drive.infrastructure.folder.persistence.FolderJpaEntity;
import com.callv2.drive.infrastructure.folder.persistence.FolderJpaRepository;

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
    public Optional<Folder> findRoot() {
        return this.folderRepository.findByRootFolderTrue().map(FolderJpaEntity::toDomain);
    }

    @Override
    public Set<Folder> findByParentFolderId(FolderID parentFolderId) {
        return this.folderRepository
                .findAllByParentFolderId(parentFolderId.getValue())
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
    public Optional<Folder> findById(FolderID id) {
        return this.folderRepository.findById(id.getValue()).map(FolderJpaEntity::toDomain);
    }

    private Folder save(Folder folder) {
        return this.folderRepository.save(FolderJpaEntity.fromDomain(folder)).toDomain();
    }

    @Override
    public Page<Folder> findAll(SearchQuery searchQuery) {
        final var page = QueryAdapter.of(searchQuery.pagination());

        final Specification<FolderJpaEntity> specification = filterService.buildSpecification(
                FolderJpaEntity.class,
                searchQuery.filterMethod(),
                searchQuery.filters());

        final org.springframework.data.domain.Page<FolderJpaEntity> pageResult = this.folderRepository.findAll(
                Specification.where(specification),
                page);

        return new Page<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalPages(),
                pageResult.getTotalElements(),
                pageResult.map(FolderJpaEntity::toDomain).toList());
    }

}
