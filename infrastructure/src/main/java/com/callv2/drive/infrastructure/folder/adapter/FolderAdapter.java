package com.callv2.drive.infrastructure.folder.adapter;

import com.callv2.drive.application.folder.create.CreateFolderInput;
import com.callv2.drive.infrastructure.folder.model.CreateFolderRequest;

public interface FolderAdapter {

    static CreateFolderInput adapt(CreateFolderRequest request) {
        return CreateFolderInput.from(request.name(), request.parentFolderId());
    }

}
