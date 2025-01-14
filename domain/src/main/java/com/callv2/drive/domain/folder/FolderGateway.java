package com.callv2.drive.domain.folder;

import java.util.Optional;

public interface FolderGateway {

    Optional<Folder> findRoot();

    Folder create(Folder folder);

    Optional<Folder> findById(FolderID id);

}
