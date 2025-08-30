package com.callv2.drive.application.folder.delete;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.Content;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.storage.StorageService;

public class DefaultDeleteFolderUseCase extends DeleteFolderUseCase {

    private final FolderGateway folderGateway;
    private final FileGateway fileGateway;
    private final StorageService storageService;

    public DefaultDeleteFolderUseCase(
            final FolderGateway folderGateway,
            final FileGateway fileGateway,
            final StorageService storageService) {
        this.folderGateway = folderGateway;
        this.fileGateway = fileGateway;
        this.storageService = storageService;
    }

    @Override
    public void execute(final DeleteFolderInput input) {

        final FolderID folderId = FolderID.of(input.id());

        final Folder folder = folderGateway
                .findById(FolderID.of(input.id()))
                .orElseThrow(() -> NotFoundException.with(Folder.class, folderId.getValue().toString()));

        final List<String> storageKeysDeleted = deleteRecursively(folder.getId());

        for (String storageKey : storageKeysDeleted) {
            this.storageService.delete(storageKey);
        }

    }

    private List<String> deleteRecursively(final FolderID folderId) {

        final List<String> storageKeysToBeDeleted = new ArrayList<>(deleteFiles(folderId));
        final Set<Folder> childrenFolders = folderGateway.findByParentFolderId(folderId);

        for (Folder children : childrenFolders) {
            storageKeysToBeDeleted.addAll(deleteRecursively(children.getId()));
        }

        this.folderGateway.deleteById(folderId);

        return storageKeysToBeDeleted;

    }

    private List<String> deleteFiles(final FolderID folderId) {

        final List<File> fileList = fileGateway.findByFolder(folderId);

        for (File file : fileList) {
            fileGateway.deleteById(file.getId());
        }

        return fileList
                .stream()
                .map(File::getContent)
                .map(Content::storageKey)
                .toList();
    }

}