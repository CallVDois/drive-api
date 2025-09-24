package com.callv2.drive.infrastructure.member.filter;

import com.callv2.drive.domain.pagination.Filter.Field;

public enum MemberField implements Field {
    ID("id"),
    USERNAME("username"),
    NICKNAME("nickname"),
    QUOTA_SIZE("quotaInBytes"),
    QUOTA_REQUEST_SIZE("quotaRequestInBytes"),
    QUOTA_REQUESTED_AT("quotaRequestedAt"),
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt"),
    HAS_SYSTEM_ACCESS("hasSystemAccess");

    private final String fieldName;

    MemberField(final String fieldName) {
        this.fieldName = fieldName;
    }

    public String value() {
        return fieldName;
    }

    @Override
    public Boolean accepts(final String name) {
        return this.name().equalsIgnoreCase(name);
    }

}
