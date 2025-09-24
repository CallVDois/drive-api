package com.callv2.drive.domain.exception;

import java.util.Arrays;
import java.util.List;

import com.callv2.drive.domain.pagination.Filter;

public class InvalidFilterOperatorException extends SilentDomainException {

    protected InvalidFilterOperatorException(String message, List<Error> errors) {
        super(message, errors);
    }

    public static InvalidFilterOperatorException with(final String filterPassed,
            final List<Filter.Operator> possibleOperators) {
        return new InvalidFilterOperatorException(
                "The filter operator is invalid.",
                List.of(
                        Error.with(
                                "Invalid filter operator: "
                                        + filterPassed
                                        + ". Possible operators are: "
                                        + Arrays.toString(possibleOperators.toArray()))));
    }

}
