package com.callv2.drive.application.folder.retrieve.get.root;

import java.util.Objects;
import java.util.Optional;

import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;

public class DefaultGetRootFolderUseCase extends GetRootFolderUseCase {

    private final FolderGateway folderGateway;

    public DefaultGetRootFolderUseCase(final FolderGateway folderGateway) {
        this.folderGateway = Objects.requireNonNull(folderGateway);
    }

    @Override
    public GetRootFolderOutput execute() {
        final Optional<Folder> root = folderGateway.findRoot();
        final Folder folder = root.isPresent() ? root.get() : folderGateway.create(Folder.createRoot());
        return GetRootFolderOutput.from(folder);
    }

}
