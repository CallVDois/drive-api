package com.callv2.drive.domain.file;

import java.util.UUID;

import com.callv2.drive.domain.Identifier;

public class FileID extends Identifier<UUID> {

    private FileID(final UUID id) {
        super(id);
    }

    public static FileID of(final UUID id) {
        return new FileID(id);
    }

    public static FileID unique() {
        return FileID.of(UUID.randomUUID());
    }

}
