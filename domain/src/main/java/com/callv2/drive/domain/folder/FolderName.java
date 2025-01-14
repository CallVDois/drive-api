package com.callv2.drive.domain.folder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.validation.Error;
import com.callv2.drive.domain.validation.ValidationHandler;

public record FolderName(String value) implements ValueObject {

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 64;

    private static final String INVALID_CHARACTERS_REGEX = "[./\\\\:*?\"<>|]";
    private static final Pattern INVALID_CHARACTERS_PATTERN = Pattern.compile(INVALID_CHARACTERS_REGEX);

    @Override
    public void validate(ValidationHandler aHandler) {

        if (value == null) {
            aHandler.append(new Error("'name' cannot be null."));
            return;
        }

        if (value.trim().isEmpty()) {
            aHandler.append(new Error("'name' cannot be empty."));
            return;
        }

        String trimmedName = value.trim();
        if (trimmedName.length() < MIN_LENGTH || trimmedName.length() > MAX_LENGTH)
            aHandler.append(
                    new Error("'name' must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters."));

        if (containsInvalidCharacters(value))
            aHandler.append(new Error("'name' contains invalid characters."));

    }

    private static boolean containsInvalidCharacters(String input) {
        Matcher matcher = INVALID_CHARACTERS_PATTERN.matcher(input);
        return matcher.find();
    }

}
