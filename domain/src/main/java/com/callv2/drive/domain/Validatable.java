package com.callv2.drive.domain;

import com.callv2.drive.domain.validation.ValidationHandler;

@FunctionalInterface
public interface Validatable {

    void validate(ValidationHandler handler);

}
