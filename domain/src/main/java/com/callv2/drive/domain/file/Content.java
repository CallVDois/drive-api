package com.callv2.drive.domain.file;

import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.validation.ValidationHandler;

public record Content(String location, String type, long size) implements ValueObject {

    public static Content of(final String location, final String type, final long size) {
        return new Content(location, type, size);
    }

    @Override
    public void validate(ValidationHandler aHandler) {
        // TODO implements validation
    }

}
