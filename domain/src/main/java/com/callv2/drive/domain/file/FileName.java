package com.callv2.drive.domain.file;

import java.util.regex.Pattern;

import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.validation.Error;
import com.callv2.drive.domain.validation.ValidationHandler;

public record FileName(String value) implements ValueObject {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 255;
    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+$");
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
    public void validate(ValidationHandler aHandler) {

        if (value == null || value.trim().isEmpty())
            aHandler.append(new Error("File name cannot be null or empty."));

        String trimmedName = value.trim();
        if (trimmedName.length() < MIN_LENGTH || trimmedName.length() > MAX_LENGTH)
            aHandler.append(
                    new Error("File name must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters."));

        if (!VALID_NAME_PATTERN.matcher(trimmedName).matches())
            aHandler.append(new Error(
                    "File name contains invalid characters. Allowed: letters, digits, dots, underscores, and hyphens."));

        if (isReservedName(trimmedName))
            aHandler.append(new Error("File name cannot be a reserved name: " + trimmedName));
    }

    private boolean isReservedName(String name) {
        for (String reserved : RESERVED_NAMES)
            if (reserved.equalsIgnoreCase(name))
                return true;

        return false;
    }

}
