package com.callv2.drive.application.file.usecase.retrieve.list;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.member.MemberID;

public record FileListOutput(
        UUID id,
        UUID ownerId,
        UUID folderId,
        String name,
        String contentType,
        Long contentSize,
        Instant createdAt,
        Instant updatedAt) {

    public static FileListOutput from(final File file, final MemberID actor) {
        return new FileListOutput(
                file.getId().getValue(),
                file.getOwner().getValue(),
                file.getVirtualFolder(actor).getValue(),
                file.getName().value(),
                file.getContent().type(),
                file.getContent().size(),
                file.getCreatedAt(),
                file.getUpdatedAt());
    }

}