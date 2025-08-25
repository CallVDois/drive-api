package com.callv2.drive.domain.file;

import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.ValidationHandler;

public record FileName(String value) implements ValueObject {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 64;
    private static final String[] RESERVED_NAMES = {
            "CON",
            "NUL",
            "PRN",
            "AUX",
            "COM1",
            "COM2",
            "COM3",
            "COM4",
            "LPT1",
            "LPT2",
            "LPT3"
    };

    public static FileName of(final String value) {
        return new FileName(value);
    }

    @Override
    public void validate(final ValidationHandler aHandler) {

        if (value == null) {
            aHandler.append(new ValidationError("'name' cannot be null."));
            return;
        }

        if (value.trim().isEmpty()) {
            aHandler.append(new ValidationError("'name' cannot be empty."));
            return;
        }

        String trimmedName = value.trim();
        if (trimmedName.length() < MIN_LENGTH || trimmedName.length() > MAX_LENGTH)
            aHandler.append(
                    new ValidationError("'name' must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters."));

        if (isReservedName(trimmedName))
            aHandler.append(new ValidationError("'name' cannot be a reserved name: " + trimmedName));
    }

    private boolean isReservedName(final String name) {
        for (String reserved : RESERVED_NAMES)
            if (reserved.equalsIgnoreCase(name))
                return true;

        return false;
    }

}
