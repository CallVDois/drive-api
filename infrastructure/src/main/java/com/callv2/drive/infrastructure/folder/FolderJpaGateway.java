package com.callv2.drive.infrastructure.folder;

import java.util.Optional;

import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;

public class FolderJpaGateway implements FolderGateway {

    @Override
    public Optional<Folder> findRoot() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findRoot'");
    }

    @Override
    public Folder create(Folder folder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public Optional<Folder> findById(FolderID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

}
