package com.callv2.drive.application.folder.retrieve.list;

import java.util.Objects;

import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.pagination.Page;

public class DefaultListFoldersUseCase extends ListFoldersUseCase {

    private final FolderGateway folderGateway;

    public DefaultListFoldersUseCase(final FolderGateway folderGateway) {
        this.folderGateway = Objects.requireNonNull(folderGateway);
    }

    @Override
    public Page<FolderListOutput> execute(final FolderListInput input) {
        return folderGateway
                .findAllWithMemberAccess(input.searchQuery(), MemberID.of(input.actorId()))
                .map(FolderListOutput::from);
    }

}
