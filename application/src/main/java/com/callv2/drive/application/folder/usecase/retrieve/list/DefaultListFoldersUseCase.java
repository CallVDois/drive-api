package com.callv2.drive.application.folder.usecase.retrieve.list;

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

        final MemberID actorId = MemberID.of(input.actorId());

        return folderGateway
                .findAllWithMemberAccess(input.searchQuery(), actorId)
                .map(folder -> FolderListOutput.from(actorId, folder));
    }

}
