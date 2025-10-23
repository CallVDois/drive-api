package com.callv2.drive.domain.folder.entity;

import java.util.UUID;

import com.callv2.drive.domain.Identifier;

public class FolderSharingID extends Identifier<UUID> {

    private FolderSharingID(final UUID id) {
        super(id);
    }

    public static FolderSharingID of(final UUID id) {
        return new FolderSharingID(id);
    }

    public static FolderSharingID unique() {
        return FolderSharingID.of(UUID.randomUUID());
    }

    @Override
    public String getStringValue() {
        return getValue().toString();
    }

    public static FolderSharingID fromStringValue(final String value) {
        return FolderSharingID.of(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return "FolderSharingID [value=" + getValue() + "]";
    }

}
