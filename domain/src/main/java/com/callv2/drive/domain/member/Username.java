package com.callv2.drive.domain.member;

import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.ValidationHandler;

public record Username(String value) implements ValueObject {

    public static Username of(final String username) {
        return new Username(username);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        if (value == null || value.isBlank())
            handler.append(ValidationError.with("'username' is required"));

        if (value.contains(" "))
            handler.append(ValidationError.with("'username' cannot contain spaces"));
    }

}
