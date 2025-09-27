package com.callv2.drive.domain.access;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.callv2.drive.domain.Identifier;
import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.ValidationHandler;

public record Resource<I>(Identifier<I> id, ResourceType type) implements ValueObject {

    public static <I> Resource<I> of(final Identifier<I> id, final ResourceType type) {
        return new Resource<>(id, type);
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(id))
            handler.append(ValidationError.with("'id' cannot be null"));

        if (isNull(type))
            handler.append(ValidationError.with("'type' cannot be null"));

        if (nonNull(id))
            id.validate(handler);

    }

}
