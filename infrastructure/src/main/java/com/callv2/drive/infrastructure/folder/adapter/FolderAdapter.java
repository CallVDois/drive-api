package com.callv2.drive.infrastructure.folder.adapter;

import java.util.UUID;

import com.callv2.drive.application.folder.usecase.create.CreateFolderInput;
import com.callv2.drive.application.folder.usecase.retrieve.get.GetFolderInput;
import com.callv2.drive.infrastructure.folder.model.CreateFolderRequest;

public interface FolderAdapter {

    static CreateFolderInput adapt(CreateFolderRequest request, UUID creatorId) {
        return CreateFolderInput.from(creatorId, request.name(), request.parentFolderId());
    }

    static GetFolderInput adapt(UUID folderId, UUID actorId) {
        return GetFolderInput.with(folderId, actorId);
    }

}
