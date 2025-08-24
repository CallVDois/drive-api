package com.callv2.drive.application.folder.retrieve.get.root;

import java.util.Objects;
import java.util.Optional;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;

public class DefaultGetRootFolderUseCase extends GetRootFolderUseCase {

    private final MemberGateway memberGateway;
    private final FolderGateway folderGateway;
    private final FileGateway fileGateway;

    public DefaultGetRootFolderUseCase(
            final MemberGateway memberGateway,
            final FolderGateway folderGateway,
            final FileGateway fileGateway) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
        this.folderGateway = Objects.requireNonNull(folderGateway);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public GetRootFolderOutput execute(final GetRootFolderInput input) {

        final MemberID owner = MemberID.of(input.ownerId());

        if (!memberGateway.existsById(owner))
            throw NotFoundException.with(Member.class, owner.getValue().toString());

        final Optional<Folder> root = folderGateway.findRoot();
        final Folder folder = root.isPresent() ? root.get() : folderGateway.create(Folder.createRoot(owner));

        return GetRootFolderOutput.from(
                folder,
                this.folderGateway.findByParentFolderId(folder.getId()),
                fileGateway.findByFolder(folder.getId()));
    }

}
