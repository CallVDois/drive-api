package com.callv2.drive.domain;

import com.callv2.drive.domain.validation.ValidationHandler;

public interface ValueObject {

    default void validate(ValidationHandler aHandler) {
    };

}
