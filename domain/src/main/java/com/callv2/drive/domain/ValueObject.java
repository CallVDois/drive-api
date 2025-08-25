package com.callv2.drive.domain;

import com.callv2.drive.domain.validation.ValidationHandler;

public interface ValueObject extends Validatable {

    default void validate(ValidationHandler handler) {
    };

}
