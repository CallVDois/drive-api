package com.callv2.drive.domain.folder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.folder.entity.FolderID;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.SearchQuery;

public interface FolderGateway {

    Optional<Folder> findMemberRootFolder(MemberID owner);

    Optional<Folder> findDefaultMemberSharedInbox(MemberID owner);

    Set<Folder> findByParentFolderId(FolderID parentFolderId);

    Set<Folder> findByParentFolderIdWithMemberAccess(FolderID parentFolderId, final MemberID actorId);

    Folder create(Folder folder);

    Folder update(Folder folder);

    void updateAll(List<Folder> folders);

    Optional<Folder> findByIdWithMemberAccess(FolderID id, MemberID actorId);

    Page<Folder> findAllWithMemberAccess(SearchQuery searchQuery, MemberID actorId);

    void deleteById(FolderID id);

}
