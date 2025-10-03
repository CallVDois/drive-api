package com.callv2.drive.infrastructure.folder.filter;

import java.util.Set;

import com.callv2.drive.domain.pagination.Filter;
import com.callv2.drive.domain.pagination.Filter.Field;

public enum FolderField implements Field {
    ID("id", Set.of(Filter.Type.EQUALS)),
    NAME("name", Set.of(Filter.Type.EQUALS, Filter.Type.LIKE)),
    OWNER_ID("ownerId", Set.of(Filter.Type.EQUALS)),
    PARENT_FOLDER_ID("parentFolderId", Set.of(Filter.Type.EQUALS)),
    CREATED_AT("createdAt", Set.of(Filter.Type.BETWEEN)),
    UPDATED_AT("updatedAt", Set.of(Filter.Type.BETWEEN));

    private final String fieldName;
    private final Set<Filter.Type> supportedTypes;

    FolderField(final String fieldName, final Set<Filter.Type> supportedTypes) {
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
