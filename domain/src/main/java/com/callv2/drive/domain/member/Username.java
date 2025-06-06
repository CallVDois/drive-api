package com.callv2.drive.domain.member;

import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.validation.Error;
import com.callv2.drive.domain.validation.ValidationHandler;

public record Username(String value) implements ValueObject {

    public static Username of(final String username) {
        return new Username(username);
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        if (value == null || value.isBlank())
            aHandler.append(Error.with("'username' is required"));

        if (value.contains(" "))
            aHandler.append(Error.with("'username' cannot contain spaces"));
    }

}
