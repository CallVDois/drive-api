package com.callv2.drive.domain.file;

import java.util.UUID;

import com.callv2.drive.domain.Identifier;

public class SharingID extends Identifier<UUID> {

    private SharingID(final UUID id) {
        super(id);
    }

    public static SharingID of(final UUID id) {
        return new SharingID(id);
    }

    public static SharingID unique() {
        return SharingID.of(UUID.randomUUID());
    }

    @Override
    public String getStringValue() {
        return getValue().toString();
    }

    public static SharingID fromStringValue(final String value) {
        return SharingID.of(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return "SharingID [value=" + getValue() + "]";
    }

}
