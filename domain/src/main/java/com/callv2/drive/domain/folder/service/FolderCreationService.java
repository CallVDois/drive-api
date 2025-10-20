package com.callv2.drive.domain.folder.service;

import java.util.Set;

import com.callv2.drive.domain.acl.AccessPermission;
import com.callv2.drive.domain.acl.Acl;
import com.callv2.drive.domain.acl.Resource;
import com.callv2.drive.domain.exception.NotAllowedException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.folder.service.result.FolderCreationResult;
import com.callv2.drive.domain.folder.valueobject.FolderName;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.handler.Notification;

public class FolderCreationService {

    public static FolderCreationResult createRootFolder(final MemberID owner) {

        final Folder folder = Folder.createRoot(owner);
        final Acl acl = Acl.create(Resource.folder(folder), folder.getOwner());

        return new FolderCreationResult(acl, folder);

    }

    public static FolderCreationResult createFolder(
            final MemberID creator,
            final FolderName name,
            final Acl parentFolderAcl,
            final Folder parentFolder,
            final Set<FolderName> siblingFolderNames) {

        final AccessPermission parentFolderAccessPermission = parentFolderAcl
                .effectiveAccessPermission(creator)
                .orElseThrow(() -> NotAllowedException
                        .with("Member [%s] doesn't have any permissions in folder [%s].".formatted(
                                creator,
                                parentFolder.getId())));

        if (!parentFolderAccessPermission.canWrite())
            throw NotAllowedException
                    .with("Member [%s] doesn't have write permission in folder [%s].".formatted(
                            creator,
                            parentFolder.getId()));

        final Notification notification = Notification.create();

        if (siblingFolderNames.contains(name))
            notification.append(
                    ValidationError.with("Folder name %s already exists in the parent folder".formatted(name.value())));

        final Folder folder = notification
                .validate(() -> Folder.create(creator, parentFolder.getOwner(), name, parentFolder));

        final Acl newFolderAcl = notification.validate(() -> parentFolderAcl.createInherited(Resource.folder(folder)));

        if (notification.hasError())
            throw ValidationException.with("Could not create Aggregate Folder", notification);

        return new FolderCreationResult(newFolderAcl, folder);

    }

    public static FolderCreationResult createInboxFolder(
            final MemberID owner,
            final Folder rootFolder,
            final Set<FolderName> siblingFolderNames) {

        Integer i = 1;
        FolderName name = FolderName.of("Shared");
        while (siblingFolderNames.contains(name)) {
            name = FolderName.of("Shared (" + ++i + ")");
        }

        final Folder folder = Folder.createInbox(owner, rootFolder.getId(), name);
        final Acl acl = Acl.create(Resource.folder(folder), folder.getOwner());

        return new FolderCreationResult(acl, folder);

    }

}
