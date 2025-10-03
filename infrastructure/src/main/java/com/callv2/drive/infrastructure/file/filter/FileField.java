package com.callv2.drive.infrastructure.file.filter;

import java.util.Set;

import com.callv2.drive.domain.pagination.Filter;
import com.callv2.drive.domain.pagination.Filter.Field;

public enum FileField implements Field {
    ID("id", Set.of(Filter.Type.EQUALS)),
    NAME("name", Set.of(Filter.Type.EQUALS, Filter.Type.LIKE)),
    OWNER_ID("ownerId", Set.of(Filter.Type.EQUALS)),
    FOLDER_ID("folderId", Set.of(Filter.Type.EQUALS)),
    TYPE("contentType", Set.of(Filter.Type.EQUALS, Filter.Type.LIKE)),
    SIZE("contentSize", Set.of(Filter.Type.EQUALS, Filter.Type.BETWEEN)),
    CREATED_AT("createdAt", Set.of(Filter.Type.BETWEEN)),
    UPDATED_AT("updatedAt", Set.of(Filter.Type.BETWEEN));

    private final String fieldName;
    private final Set<Filter.Type> supportedTypes;

    FileField(final String fieldName, final Set<Filter.Type> supportedTypes) {
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
