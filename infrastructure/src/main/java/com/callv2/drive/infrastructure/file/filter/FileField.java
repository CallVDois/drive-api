package com.callv2.drive.infrastructure.file.filter;

import com.callv2.drive.domain.pagination.Filter.Field;

public enum FileField implements Field {
    ID("id"),
    NAME("name"),
    OWNER_ID("ownerId"),
    FOLDER_ID("folderId"),
    TYPE("contentType"),
    SIZE("contentSize"),
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt");

    private final String fieldName;

    FileField(final String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Boolean accepts(final String name) {

        if (name == null)
            return false;

        return this.name().equalsIgnoreCase(name);

    }

}
