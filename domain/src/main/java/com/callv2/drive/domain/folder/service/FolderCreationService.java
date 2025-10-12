package com.callv2.drive.domain.folder.service;

import java.util.Set;

import com.callv2.drive.domain.acl.Acl;
import com.callv2.drive.domain.acl.Resource;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.folder.service.result.FolderCreationResult;
import com.callv2.drive.domain.folder.valueobject.FolderName;
import com.callv2.drive.domain.member.MemberID;

public class FolderCreationService {

    public static FolderCreationResult createRootFolder(final MemberID owner) {

        final Folder folder = Folder.createRoot(owner);
        final Acl acl = Acl.create(Resource.folder(folder), folder.getOwner());

        return new FolderCreationResult(acl, folder);

    }

    public static FolderCreationResult createInboxFolder(
            final MemberID owner,
            final Folder rootFolder,
            final Set<FolderName> parentFolderNames) {

        Integer i = 1;
        FolderName name = FolderName.of("Shared");
        while (parentFolderNames.contains(name)) {
            name = FolderName.of("Shared (" + ++i + ")");
        }

        final Folder folder = Folder.createInbox(owner, rootFolder.getId(), name);
        final Acl acl = Acl.create(Resource.folder(folder), folder.getOwner());

        return new FolderCreationResult(acl, folder);

    }

}
