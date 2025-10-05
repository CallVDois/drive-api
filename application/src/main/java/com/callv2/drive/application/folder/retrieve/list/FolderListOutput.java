package com.callv2.drive.application.folder.retrieve.list;

import java.util.UUID;

import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.member.MemberID;

public record FolderListOutput(
        UUID id,
        String name,
        UUID parentFolder) {

    public static FolderListOutput from(final MemberID actorId, final Folder folder) {
        return new FolderListOutput(
                folder.getId().getValue(),
                folder.getName().value(),
                folder.getVirtualParentFolder(actorId).getValue());
    }

    public static record SubFolder(UUID id, String name) {

        public static FolderListOutput.SubFolder from(final Folder subFolder) {
            return new FolderListOutput.SubFolder(subFolder.getId().getValue(), subFolder.getName().value());
        }

    }

}
