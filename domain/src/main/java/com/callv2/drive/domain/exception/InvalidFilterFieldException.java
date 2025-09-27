package com.callv2.drive.domain.exception;

import java.util.Arrays;
import java.util.List;

import com.callv2.drive.domain.pagination.Filter;

public class InvalidFilterFieldException extends InvalidFilterException {

    protected InvalidFilterFieldException(final String filterPassed, final List<Filter.Field> possibleFields) {
        super(
                "The filter field is invalid.",
                List.of(Error.with(
                        "Invalid filter field: " +
                                filterPassed +
                                ". Possible fields are: " +
                                Arrays.toString(possibleFields.toArray()))));
    }

    public static InvalidFilterFieldException with(final String fieldPassed, final List<Filter.Field> possibleFields) {
        return new InvalidFilterFieldException(fieldPassed, possibleFields);
    }

}
