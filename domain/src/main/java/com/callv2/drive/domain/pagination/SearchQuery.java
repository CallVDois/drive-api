package com.callv2.drive.domain.pagination;

import java.util.Arrays;
import java.util.List;

public record SearchQuery(
        int page,
        int perPage,
        Order order,
        Filter<?>... filters) {

    public List<Filter<?>> filtersList() {
        return filters == null ? List.of() : Arrays.asList(filters);
    }

    public record Filter<T extends Comparable<T>>(String field, T value, T valueToCompare, Type type) {

        public T valueToCompare() {
            return valueToCompare == null ? value : valueToCompare;
        }

        public enum Type {
            EQUALS, LIKE, BETWEEN
        }

    }

    public record Order(String field, Direction direction) {

        public enum Direction {
            ASC, DESC
        }
    }

}
