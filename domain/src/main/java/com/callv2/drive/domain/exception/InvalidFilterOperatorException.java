package com.callv2.drive.domain.exception;

import java.util.Arrays;
import java.util.List;

import com.callv2.drive.domain.pagination.Filter;

public class InvalidFilterOperatorException extends InvalidFilterException {

    protected InvalidFilterOperatorException(final String filterPassed, final List<Filter.Operator> possibleOperators) {
        super(
                "The filter operator is invalid.",
                List.of(Error.with(
                        "Invalid filter operator: "
                                + filterPassed
                                + ". Possible operators are: "
                                + Arrays.toString(possibleOperators.toArray()))));
    }

    public static InvalidFilterOperatorException with(
            final String filterPassed,
            final List<Filter.Operator> possibleOperators) {
        return new InvalidFilterOperatorException(filterPassed, possibleOperators);
    }

}
