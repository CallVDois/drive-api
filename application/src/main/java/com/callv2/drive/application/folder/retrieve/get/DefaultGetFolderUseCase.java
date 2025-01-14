package com.callv2.drive.application.folder.retrieve.get;

import java.util.Objects;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;

public class DefaultGetFolderUseCase extends GetFolderUseCase {

    private final FolderGateway folderGateway;

    public DefaultGetFolderUseCase(final FolderGateway folderGateway) {
        this.folderGateway = Objects.requireNonNull(folderGateway);
    }

    @Override
    public GetFolderOutput execute(GetFolderInput input) {
        return GetFolderOutput
                .from(folderGateway
                        .findById(FolderID.of(input.id()))
                        .orElseThrow(() -> NotFoundException.with(Folder.class, input.id().toString())));
    }

}
