package com.callv2.drive.infrastructure.folder.filter;

import com.callv2.drive.domain.pagination.Filter.Field;

public enum FolderField implements Field {
    ID("id"),
    NAME("name"),
    OWNER_ID("ownerId"),
    PARENT_FOLDER_ID("parentFolderId"),
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt");

    private final String fieldName;

    FolderField(final String fieldName) {
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
