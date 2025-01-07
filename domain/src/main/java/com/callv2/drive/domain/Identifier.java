package com.callv2.drive.domain;

import java.util.Objects;

import com.callv2.drive.domain.validation.Error;
import com.callv2.drive.domain.validation.ValidationHandler;

public abstract class Identifier<T> implements ValueObject {

    protected T id;

    protected Identifier(final T id) {
        this.id = id;
    }

    public T getValue() {
        return id;
    }

    @Override
    public void validate(ValidationHandler aHandler) {
        if (Objects.isNull(this.id))
            aHandler.append(new Error("'id' should not be null"));
    }

}
