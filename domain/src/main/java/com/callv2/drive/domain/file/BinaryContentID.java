package com.callv2.drive.domain.file;

import java.util.UUID;

import com.callv2.drive.domain.Identifier;

public class BinaryContentID extends Identifier<UUID> {

    private BinaryContentID(final UUID id) {
        super(id);
    }

    public static BinaryContentID of(final UUID id) {
        return new BinaryContentID(id);
    }

    public static BinaryContentID unique() {
        return BinaryContentID.of(UUID.randomUUID());
    }
}
