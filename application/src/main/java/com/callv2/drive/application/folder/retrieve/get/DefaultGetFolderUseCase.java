package com.callv2.drive.application.folder.retrieve.get;

import java.util.Objects;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;

public class DefaultGetFolderUseCase extends GetFolderUseCase {

    private final FolderGateway folderGateway;
    private final FileGateway fileGateway;

    public DefaultGetFolderUseCase(
            final FolderGateway folderGateway,
            final FileGateway fileGateway) {
        this.folderGateway = Objects.requireNonNull(folderGateway);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public GetFolderOutput execute(GetFolderInput input) {

        final Folder folder = folderGateway
                .findById(FolderID.of(input.id()))
                .orElseThrow(() -> NotFoundException.with(Folder.class, input.id().toString()));

        return GetFolderOutput
                .from(
                        folder,
                        folderGateway.findByParentFolderId(folder.getId()),
                        fileGateway.findByFolder(folder.getId()));
    }

}
