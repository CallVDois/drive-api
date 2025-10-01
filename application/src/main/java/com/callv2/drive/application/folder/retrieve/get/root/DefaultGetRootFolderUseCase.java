package com.callv2.drive.application.folder.retrieve.get.root;

import java.util.Objects;
import java.util.Optional;

import com.callv2.drive.domain.access.Acl;
import com.callv2.drive.domain.access.AclGateway;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;

public class DefaultGetRootFolderUseCase extends GetRootFolderUseCase {

    private final EventDispatcher eventDispatcher;

    private final AclGateway aclGateway;
    private final MemberGateway memberGateway;
    private final FolderGateway folderGateway;
    private final FileGateway fileGateway;

    public DefaultGetRootFolderUseCase(
            final EventDispatcher eventDispatcher,
            final AclGateway aclGateway,
            final MemberGateway memberGateway,
            final FolderGateway folderGateway,
            final FileGateway fileGateway) {
        this.eventDispatcher = Objects.requireNonNull(eventDispatcher);
        this.aclGateway = Objects.requireNonNull(aclGateway);
        this.memberGateway = Objects.requireNonNull(memberGateway);
        this.folderGateway = Objects.requireNonNull(folderGateway);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public GetRootFolderOutput execute(final GetRootFolderInput input) {

        final MemberID owner = MemberID.of(input.ownerId());

        if (!memberGateway.existsById(owner))
            throw NotFoundException.with(Member.class, owner.getValue().toString());

        final Optional<Folder> root = folderGateway.findMemberRootFolder(owner);
        final Folder folder = root.isPresent() ? root.get() : createRoot(owner);

        return GetRootFolderOutput.from(
                folder,
                this.folderGateway.findByParentFolderIdWithMemberAccess(folder.getId(), owner),
                fileGateway.findAllActiveByFolder(folder.getId()));

    }

    private Folder createRoot(final MemberID owner) {

        final Folder root = Folder.createRoot(owner);
        eventDispatcher.notify(aclGateway.create(Acl.create(Resource.folder(root.getId())).grantTotal(owner)));
        return folderGateway.create(root);

    }

}
