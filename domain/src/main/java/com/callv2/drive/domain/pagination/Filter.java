package com.callv2.drive.domain.pagination;

import java.util.Arrays;
import java.util.Optional;

import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.handler.Notification;

public record Filter(String field, String value, String valueToCompare, Type type) {

    public Filter {

        final Notification notification = Notification.create();
        if (field == null)
            notification.append(ValidationError.with("Filter.field cannot be null"));

        if (field != null && field.isBlank())
            notification.append(ValidationError.with("Filter.field cannot be blank"));

        if (value == null)
            notification.append(ValidationError.with("Filter.value cannot be null"));

        if (type == null)
            notification.append(
                    ValidationError.with(
                            "Filter.type cannot be null, Valid values are: " + Arrays.toString(Type.values())));

        if (notification.hasError())
            throw ValidationException.with("Filter contains invalid parameters", notification);

    }

    public String valueToCompare() {
        return valueToCompare == null ? value : valueToCompare;
    }

    public enum Type {

        EQUALS,
        LIKE,
        BETWEEN;

        public static Optional<Type> of(final String type) {
            if (type == null)
                return Optional.empty();

            return Arrays.stream(Type.values())
                    .filter(it -> it.name().equalsIgnoreCase(type))
                    .findFirst();
        }

    }

    public enum Operator {
        AND, OR;

        public static Optional<Operator> of(final String type) {
            if (type == null)
                return Optional.empty();

            return Arrays.stream(Operator.values())
                    .filter(it -> it.name().equalsIgnoreCase(type))
                    .findFirst();
        }
    }

}
