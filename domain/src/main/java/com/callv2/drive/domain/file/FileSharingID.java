package com.callv2.drive.domain.file;

import java.util.UUID;

import com.callv2.drive.domain.Identifier;

public class FileSharingID extends Identifier<UUID> {

    private FileSharingID(final UUID id) {
        super(id);
    }

    public static FileSharingID of(final UUID id) {
        return new FileSharingID(id);
    }

    public static FileSharingID unique() {
        return FileSharingID.of(UUID.randomUUID());
    }

    @Override
    public String getStringValue() {
        return getValue().toString();
    }

    public static FileSharingID fromStringValue(final String value) {
        return FileSharingID.of(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return "SharingID [value=" + getValue() + "]";
    }

}
