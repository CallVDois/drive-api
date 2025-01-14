package com.callv2.drive.domain.folder;

import java.util.UUID;

import com.callv2.drive.domain.Identifier;

public class FolderID extends Identifier<UUID> {

    public FolderID(UUID value) {
        super(value);
    }

    public static FolderID of(final UUID id) {
        return new FolderID(id);
    }

    public static FolderID unique() {
        return FolderID.of(UUID.randomUUID());
    }

}
