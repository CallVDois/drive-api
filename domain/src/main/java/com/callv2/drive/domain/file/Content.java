package com.callv2.drive.domain.file;

import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.validation.ValidationHandler;
import com.callv2.drive.domain.validation.ValidationError;

public record Content(String storageKey, String type, long size) implements ValueObject {

    public static Content of(final String storageKey, final String type, final long size) {
        return new Content(storageKey, type, size);
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        validateStorageKey(aHandler);
        validateType(aHandler);
    }

    private void validateStorageKey(final ValidationHandler aHandler) {
        if (storageKey == null) {
            aHandler.append(new ValidationError("'storageKey' cannot be null."));
            return;
        }

        if (storageKey.trim().isEmpty()) {
            aHandler.append(new ValidationError("'location' cannot be empty."));
            return;
        }
    }

    private void validateType(final ValidationHandler aHandler) {
        if (type == null) {
            aHandler.append(new ValidationError("'type' cannot be null."));
            return;
        }

        if (type.trim().isEmpty()) {
            aHandler.append(new ValidationError("'type' cannot be empty."));
            return;
        }
    }
}
