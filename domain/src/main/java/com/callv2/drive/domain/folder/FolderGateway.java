package com.callv2.drive.domain.folder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.SearchQuery;

public interface FolderGateway {

    Optional<Folder> findRoot();

    Set<Folder> findByParentFolderId(FolderID parentFolderId);

    Folder create(Folder folder);

    Folder update(Folder folder);

    void updateAll(List<Folder> folders);

    Optional<Folder> findById(FolderID id);

    Page<Folder> findAll(final SearchQuery searchQuery);

    void deleteById(FolderID id);

}
