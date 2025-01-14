package com.callv2.drive.domain.file;

import java.util.List;
import java.util.Optional;

import com.callv2.drive.domain.folder.FolderID;

public interface FileGateway {

    File create(File file);

    Optional<File> findById(FileID id);

    List<File> findByFolder(FolderID folderId);

}
