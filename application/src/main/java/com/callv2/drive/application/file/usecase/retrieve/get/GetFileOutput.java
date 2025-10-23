package com.callv2.drive.application.file.usecase.retrieve.get;

import java.time.Instant;
import java.util.UUID;

import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.member.MemberID;

public record GetFileOutput(
        UUID id,
        UUID ownerId,
        UUID folderId,
        String name,
        String contentType,
        Long contentSize,
        UUID creatorId,
        Instant createdAt,
        UUID updaterId,
        Instant updatedAt,
        UUID deleterId,
        Instant deletedAt) {

    public static GetFileOutput from(final File file, final MemberID actor) {
        return new GetFileOutput(
                file.getId().getValue(),
                file.getOwner().getValue(),
                file.getVirtualFolder(actor).getValue(),
                file.getName().value(),
                file.getContent().type(),
                file.getContent().size(),
                file.getCreator().getValue(),
                file.getCreatedAt(),
                file.getUpdatedBy().getValue(),
                file.getUpdatedAt(),
                file.getDeletedBy() != null ? file.getDeletedBy().getValue() : null,
                file.getDeletedAt());
    }

}
