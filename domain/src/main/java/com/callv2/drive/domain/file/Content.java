package com.callv2.drive.domain.file;

import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.validation.ValidationHandler;
import com.callv2.drive.domain.validation.Error;

public record Content(String location, String type, long size) implements ValueObject {

    public static Content of(final String location, final String type, final long size) {
        return new Content(location, type, size);
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        validateLocation(aHandler);
        validateType(aHandler);
    }

    private void validateLocation(final ValidationHandler aHandler) {
        if (location == null) {
            aHandler.append(new Error("'location' cannot be null."));
            return;
        }

        if (location.trim().isEmpty()) {
            aHandler.append(new Error("'location' cannot be empty."));
            return;
        }
    }

    private void validateType(final ValidationHandler aHandler) {
        if (type == null) {
            aHandler.append(new Error("'type' cannot be null."));
            return;
        }

        if (type.trim().isEmpty()) {
            aHandler.append(new Error("'type' cannot be empty."));
            return;
        }
    }
}
