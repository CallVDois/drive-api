package com.callv2.drive.domain.exception;

import java.util.Arrays;
import java.util.List;

import com.callv2.drive.domain.pagination.Filter;

public class InvalidFilterException extends SilentDomainException {

    private InvalidFilterException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static InvalidFilterException filter(final String filter) {
        return new InvalidFilterException("The filter is invalid.", List.of(Error.with("Invalid filter: " + filter)));
    }

    public static InvalidFilterException field(
            final String fieldPassed,
            final List<Filter.Field> possibleFields) {
        return new InvalidFilterException(
                "The filter field is invalid.",
                List.of(Error.with(
                        "Invalid filter field: "
                                + fieldPassed
                                + ". Possible fields are: " +
                                Arrays.toString(possibleFields.toArray()))));
    }

    public static InvalidFilterException operator(
            final String filterPassed,
            final List<Filter.Operator> possibleOperators) {
        return new InvalidFilterException(
                "The filter operator is invalid.",
                List.of(Error.with(
                        "Invalid filter operator: "
                                + filterPassed
                                + ". Possible operators are: "
                                + Arrays.toString(possibleOperators.toArray()))));
    }

    public static InvalidFilterException type(
            final String typePassed,
            final List<Filter.Type> possibleTypes) {
        return new InvalidFilterException(
                "The filter type is invalid.",
                List.of(Error.with(
                        "Invalid filter type: "
                                + typePassed
                                + ". Possible types are: "
                                + Arrays.toString(possibleTypes.toArray()))));
    }

}
