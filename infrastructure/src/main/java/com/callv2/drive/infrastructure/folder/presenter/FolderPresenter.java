package com.callv2.drive.infrastructure.folder.presenter;

import com.callv2.drive.application.folder.create.CreateFolderOutput;
import com.callv2.drive.infrastructure.folder.model.CreateFolderResponse;

public interface FolderPresenter {

    static CreateFolderResponse present(CreateFolderOutput output) {
        return new CreateFolderResponse(output.id());
    }

}
