package com.callv2.drive.domain.access;

import java.util.UUID;

import com.callv2.drive.domain.Identifier;

public class EntryID extends Identifier<UUID> {

    private EntryID(final UUID id) {
        super(id);
    }

    public static EntryID of(final UUID id) {
        return new EntryID(id);
    }

    @Override
    public String getStringValue() {
        return getValue().toString();
    }

    public static EntryID fromStringValue(String value) {
        return EntryID.of(UUID.fromString(value));
    }

    public static EntryID unique() {
        return EntryID.of(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return "EntryID [value=" + getStringValue() + "]";
    }

}
