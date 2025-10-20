package com.callv2.drive.infrastructure.member.filter;

import java.util.Set;

import com.callv2.drive.domain.pagination.Filter;
import com.callv2.drive.domain.pagination.Filter.Field;

public enum MemberField implements Field {
    ID("id", Set.of(Filter.Type.EQUALS)),
    USERNAME("username", Set.of(Filter.Type.EQUALS, Filter.Type.LIKE)),
    NICKNAME("nickname", Set.of(Filter.Type.EQUALS, Filter.Type.LIKE)),
    QUOTA_SIZE("quotaInBytes", Set.of(Filter.Type.EQUALS, Filter.Type.BETWEEN)),
    QUOTA_REQUEST_SIZE("quotaRequestInBytes", Set.of(Filter.Type.EQUALS, Filter.Type.BETWEEN)),
    QUOTA_REQUESTED_AT("quotaRequestedAt", Set.of(Filter.Type.BETWEEN)),
    CREATED_AT("createdAt", Set.of(Filter.Type.BETWEEN)),
    UPDATED_AT("updatedAt", Set.of(Filter.Type.BETWEEN)),
    HAS_SYSTEM_ACCESS("hasSystemAccess", Set.of(Filter.Type.EQUALS));

    private final String fieldName;
    private final Set<Filter.Type> supportedTypes;

    MemberField(final String fieldName, final Set<Filter.Type> supportedTypes) {
        this.fieldName = fieldName;
        this.supportedTypes = supportedTypes;
    }

    public String value() {
        return fieldName;
    }

    public Set<Filter.Type> supportedTypes() {
        return supportedTypes;
    }

}
