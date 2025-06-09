package com.callv2.drive.domain.folder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.ValidationHandler;

public record FolderName(String value) implements ValueObject {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 64;

    private static final String INVALID_CHARACTERS_REGEX = "[./\\\\:*?\"<>|]";
    private static final Pattern INVALID_CHARACTERS_PATTERN = Pattern.compile(INVALID_CHARACTERS_REGEX);

    public static FolderName of(final String value) {
        return new FolderName(value);
    }

    @Override
    public void validate(ValidationHandler handler) {

        if (value == null) {
            handler.append(new ValidationError("'name' cannot be null."));
            return;
        }

        if (value.trim().isEmpty()) {
            handler.append(new ValidationError("'name' cannot be empty."));
            return;
        }

        String trimmedName = value.trim();
        if (trimmedName.length() < MIN_LENGTH || trimmedName.length() > MAX_LENGTH)
            handler.append(
                    new ValidationError(
                            "'name' must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters."));

        if (containsInvalidCharacters(value))
            handler.append(new ValidationError("'name' contains invalid characters."));

    }

    private static boolean containsInvalidCharacters(String input) {
        Matcher matcher = INVALID_CHARACTERS_PATTERN.matcher(input);
        return matcher.find();
    }

}
