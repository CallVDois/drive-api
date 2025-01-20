package com.callv2.drive.application.folder.retrieve.get.root;

import java.util.Objects;
import java.util.Optional;

import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;

public class DefaultGetRootFolderUseCase extends GetRootFolderUseCase {

    private final FolderGateway folderGateway;
    private final FileGateway fileGateway;

    public DefaultGetRootFolderUseCase(
            final FolderGateway folderGateway,
            final FileGateway fileGateway) {
        this.folderGateway = Objects.requireNonNull(folderGateway);
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public GetRootFolderOutput execute() {
        final Optional<Folder> root = folderGateway.findRoot();
        final Folder folder = root.isPresent() ? root.get() : folderGateway.create(Folder.createRoot());
        return GetRootFolderOutput.from(folder, fileGateway.findByFolder(folder.getId()));
    }

}
