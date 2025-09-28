package com.callv2.drive.domain.member;

import java.util.UUID;

import com.callv2.drive.domain.Identifier;

public class MemberID extends Identifier<UUID> {

    public MemberID(final UUID value) {
        super(value);
    }

    public static MemberID of(final UUID id) {
        return new MemberID(id);
    }

    @Override
    public String getStringValue() {
        return getValue().toString();
    }

    @Override
    public String toString() {
        return "MemberID [value=" + getValue() + "]";
    }

}
