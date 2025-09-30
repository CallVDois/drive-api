package com.callv2.drive.application.folder.create;

import java.util.Set;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.folder.FolderName;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.ValidationError;
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
        final MemberID creatorId = MemberID.of(input.creatorId());

        if (!memberGateway.existsById(creatorId))
            throw NotFoundException.with(Member.class, input.creatorId().toString());

        final Folder parentFolder = folderGateway
                .findByIdWithMemberAccess(FolderID.of(input.parentFolderId()), creatorId)
                .orElseThrow(() -> NotFoundException.with(
                        Folder.class,
                        "Parent folder with id %s not found".formatted(input.parentFolderId().toString())));

        return CreateFolderOutput.from(createFolder(creatorId, FolderName.of(input.name()), parentFolder));
    }

    private Folder createFolder(final MemberID creatorId, FolderName name, final Folder parentFolder) {

        final Notification notification = Notification.create();

        final Set<Folder> subFolders = folderGateway.findByParentFolderId(parentFolder.getId());

        if (subFolders.stream().anyMatch(subFolder -> subFolder.getName().equals(name)))
            notification.append(ValidationError.with("Folder with the same name already exists"));

        final Folder folder = notification
                .validate(() -> Folder.create(creatorId, parentFolder.getOwner(), name, parentFolder));

        if (notification.hasError())
            throw ValidationException.with("Could not create Aggregate Folder", notification);

        folderGateway.update(parentFolder);
        return folderGateway.create(folder);
    }

}
