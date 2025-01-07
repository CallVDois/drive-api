package com.callv2.drive.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(final Error anError);

    ValidationHandler append(final ValidationHandler anHandler);

    <T> T valdiate(final Validation<T> aValidation);

    List<Error> getErrors();

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    @FunctionalInterface
    public interface Validation<T> {

        T validate();

    }

}
