package com.callv2.drive.domain.exception;

import java.util.Arrays;
import java.util.List;

import com.callv2.drive.domain.pagination.Filter;

public class InvalidFilterFieldException extends SilentDomainException {

    protected InvalidFilterFieldException(String message, List<Error> errors) {
        super(message, errors);
    }

    public static InvalidFilterFieldException with(final String filterPassed, final List<Filter.Field> possibleFields) {
        return new InvalidFilterFieldException(
                "The filter field is invalid.",
                List.of(Error.with("Invalid filter field: " + filterPassed + ". Possible fields are: "
                        + Arrays.toString(possibleFields.toArray()))));
    }

}
