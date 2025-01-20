package com.callv2.drive.domain.pagination;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.validation.Error;
import com.callv2.drive.domain.validation.handler.Notification;

public record SearchQuery(
        int page,
        int perPage,
        Order order,
        FilterMethod filterMethod,
        List<Filter> filters) {

    public static SearchQuery of(
            final int page,
            final int perPage,
            final Order order,
            final FilterMethod filterMethod,
            final List<Filter> filters) {
        return new SearchQuery(page, perPage, order, filterMethod, filters);
    }

    public enum FilterMethod {
        AND, OR;

        public static Optional<FilterMethod> of(final String type) {
            if (type == null)
                return Optional.empty();

            return Arrays.stream(FilterMethod.values())
                    .filter(it -> it.name().equalsIgnoreCase(type))
                    .findFirst();
        }
    }

    public record Filter(String field, String value, String valueToCompare, Type type) {

        public Filter {

            final Notification notification = Notification.create();
            if (field == null)
                notification.append(Error.with("Filter.field cannot be null"));

            if (field != null && field.isBlank())
                notification.append(Error.with("Filter.field cannot be blank"));

            if (value == null)
                notification.append(Error.with("Filter.value cannot be null"));

            if (type == null)
                notification.append(
                        Error.with(
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

    }

    public record Order(String field, Direction direction) {

        public static Order of(final String field, final Direction direction) {
            return new Order(field, direction);
        }

        public enum Direction {
            ASC, DESC
        }
    }

}
