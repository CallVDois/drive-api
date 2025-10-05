package com.callv2.drive.application.sharing.file.retrieve.list.received;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.application.file.retrieve.list.FileListOutput;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.folder.FolderID;

public record FileSharingReceivedListOutput(
        UUID id,
        UUID ownerId,
        UUID folderId,
        String name,
        String contentType,
        Long contentSize,
        Instant createdAt,
        Instant updatedAt) {

    public static FileListOutput from(final File file, final FolderID virtualFolder) {
        return new FileListOutput(
                file.getId().getValue(),
                file.getOwner().getValue(),
                virtualFolder.getValue(),
                file.getName().value(),
                file.getContent().type(),
                file.getContent().size(),
                file.getCreatedAt(),
                file.getUpdatedAt());
    }

}
