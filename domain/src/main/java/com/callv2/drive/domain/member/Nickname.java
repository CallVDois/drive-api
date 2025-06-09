package com.callv2.drive.domain.member;

import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.ValidationHandler;

public record Nickname(String value) implements ValueObject {

    public static Nickname of(final String nickname) {
        return new Nickname(nickname);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        if (value == null || value.isBlank())
            handler.append(ValidationError.with("'nickname' is required"));
    }

}
