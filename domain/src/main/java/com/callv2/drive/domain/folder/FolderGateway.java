package com.callv2.drive.domain.folder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.SearchQuery;

public interface FolderGateway {

    Optional<Folder> findRoot(MemberID owner);

    Set<Folder> findByParentFolderId(FolderID parentFolderId);

    Folder create(Folder folder);

    Folder update(Folder folder);

    void updateAll(List<Folder> folders);

    Optional<Folder> findByIdWithMemberAccess(FolderID id, MemberID actorId);

    Page<Folder> findAllWithMemberAccess(SearchQuery searchQuery, MemberID actorId);

    void deleteById(FolderID id);

}
