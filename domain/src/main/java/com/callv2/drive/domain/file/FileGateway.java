package com.callv2.drive.domain.file;

import java.util.List;
import java.util.Optional;

import com.callv2.drive.domain.folder.entity.FolderID;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.SearchQuery;

public interface FileGateway {

    File create(File file);

    File update(File file);

    Optional<File> findByIdWithMemberAccess(final FileID id, final MemberID memberId);

    List<File> findAllByFolder(FolderID folderId);

    List<File> findByOwner(MemberID ownerId);

    Page<File> findAllWithMemberAccess(SearchQuery searchQuery, MemberID memberId);

    void deleteById(FileID id);

    Long sumAllContentSize();

}
