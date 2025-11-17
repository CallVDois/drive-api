package com.callv2.drive.application.folder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.member.MemberID;

public class PathResolutionApplicationService {

    private final FolderGateway folderGateway;

    public PathResolutionApplicationService(final FolderGateway folderGateway) {
        this.folderGateway = Objects.requireNonNull(folderGateway);
    }

    public List<Folder> resolvePath(final Folder folder, final MemberID actorId) {

        Folder parentFolder = folder;
        final List<Folder> pathFolders = new ArrayList<>(List.of(folder));

        if (parentFolder.isRootFolder())
            return pathFolders;

        do {

            parentFolder = folderGateway
                    .findByIdWithMemberAccess(parentFolder.getVirtualParentFolder(actorId), actorId)
                    .orElse(parentFolder);

            pathFolders.add(parentFolder);

        } while (!parentFolder.isRootFolder());

        return pathFolders;

    }

}
