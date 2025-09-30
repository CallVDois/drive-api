package com.callv2.drive.application.folder.delete;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.MemberID;

public class DefaultDeleteFolderUseCase extends DeleteFolderUseCase {

    private final FolderGateway folderGateway;
    private final FileGateway fileGateway;
    private final EventDispatcher eventDispatcher;

    public DefaultDeleteFolderUseCase(
            final FolderGateway folderGateway,
            final FileGateway fileGateway,
            final EventDispatcher eventDispatcher) {
        this.folderGateway = folderGateway;
        this.fileGateway = fileGateway;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void execute(final DeleteFolderInput input) {

        final FolderID folderId = FolderID.of(input.id());
        final MemberID actorId = MemberID.of(input.actorId());

        final Folder folder = folderGateway
                .findByIdWithMemberAccess(folderId, actorId)
                .orElseThrow(() -> NotFoundException.with(Folder.class, folderId.getValue().toString()));

        final List<File> deletedFiles = deleteRecursively(folder.getId());

        for (File deletedFile : deletedFiles) {
            eventDispatcher.notify(deletedFile);
        }

    }

    private List<File> deleteRecursively(final FolderID folderId) {

        final List<File> storageKeysToBeDeleted = new ArrayList<>(deleteFiles(folderId));
        final Set<Folder> childrenFolders = folderGateway.findByParentFolderId(folderId);

        for (Folder children : childrenFolders) {
            storageKeysToBeDeleted.addAll(deleteRecursively(children.getId()));
        }

        this.folderGateway.deleteById(folderId);

        return storageKeysToBeDeleted;

    }

    private List<File> deleteFiles(final FolderID folderId) {

        final List<File> fileList = fileGateway.findByFolder(folderId);

        for (File file : fileList) {
            fileGateway.update(file.delete());
        }

        return fileList;
    }

}