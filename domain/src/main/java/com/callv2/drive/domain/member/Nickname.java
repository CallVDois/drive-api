package com.callv2.drive.domain.member;

import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.validation.Error;
import com.callv2.drive.domain.validation.ValidationHandler;

public record Nickname(String value) implements ValueObject {

    public static Nickname of(final String nickname) {
        return new Nickname(nickname);
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        if (value == null || value.isBlank())
            aHandler.append(Error.with("'nickname' is required"));
    }

}
