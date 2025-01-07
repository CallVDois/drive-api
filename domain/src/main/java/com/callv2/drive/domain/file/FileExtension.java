package com.callv2.drive.domain.file;

import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.validation.Error;
import com.callv2.drive.domain.validation.ValidationHandler;

public record FileExtension(String value) implements ValueObject {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 255;

    public static FileExtension of(final String value) {
        return new FileExtension(value);
    }

    @Override
    public void validate(ValidationHandler aHandler) {

        if (value == null || value.trim().isEmpty())
            aHandler.append(new Error("File extension cannot be null or empty."));

        String trimmedValue = value.trim();
        if (trimmedValue.length() < MIN_LENGTH || trimmedValue.length() > MAX_LENGTH)
            aHandler.append(
                    new Error("File extension must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters."));

    }

}
