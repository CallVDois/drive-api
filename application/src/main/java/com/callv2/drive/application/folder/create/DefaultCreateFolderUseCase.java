package com.callv2.drive.application.folder.create;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.folder.FolderName;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.Error;
import com.callv2.drive.domain.validation.handler.Notification;

public class DefaultCreateFolderUseCase extends CreateFolderUseCase {

    private final MemberGateway memberGateway;
    private final FolderGateway folderGateway;

    public DefaultCreateFolderUseCase(final MemberGateway memberGateway, final FolderGateway folderGateway) {
        this.memberGateway = memberGateway;
        this.folderGateway = folderGateway;
    }

    @Override
    public CreateFolderOutput execute(final CreateFolderInput input) {
        final MemberID ownerId = MemberID.of(input.ownerId());

        if (!memberGateway.existsById(ownerId))
            throw NotFoundException.with(Member.class, input.ownerId().toString());

        final Folder parentFolder = folderGateway
                .findById(FolderID.of(input.parentFolderId()))
                .orElseThrow(() -> NotFoundException.with(
                        Folder.class,
                        "Parent folder with id %s not found".formatted(input.parentFolderId().toString())));

        return CreateFolderOutput.from(createFolder(ownerId, FolderName.of(input.name()), parentFolder));
    }

    private Folder createFolder(final MemberID ownerId, FolderName name, final Folder parentFolder) {
        final Notification notification = Notification.create();
        if (parentFolder.getSubFolders().stream().anyMatch(subFolder -> subFolder.name().equals(name)))
            notification.append(Error.with("Folder with the same name already exists"));

        final Folder folder = notification.valdiate(() -> Folder.create(ownerId, name, parentFolder));

        if (notification.hasError())
            throw ValidationException.with("Could not create Aggregate Folder", notification);

        folderGateway.update(parentFolder);
        return folderGateway.create(folder);
    }

}
