package com.callv2.drive.domain.access;

import java.util.UUID;

import com.callv2.drive.domain.Identifier;

public class AclID extends Identifier<UUID> {

    public AclID(UUID value) {
        super(value);
    }

    public static AclID of(final UUID id) {
        return new AclID(id);
    }

    @Override
    public String getStringValue() {
        return getValue().toString();
    }

    public static AclID fromStringValue(String value) {
        return AclID.of(UUID.fromString(value));
    }

    public static AclID unique() {
        return AclID.of(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return "AccessID [value=" + getStringValue() + "]";
    }

}
