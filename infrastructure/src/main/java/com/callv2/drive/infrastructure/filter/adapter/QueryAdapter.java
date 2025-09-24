package com.callv2.drive.infrastructure.filter.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.callv2.drive.domain.pagination.Filter;
import com.callv2.drive.domain.pagination.Pagination;

public interface QueryAdapter {

    static PageRequest of(final Pagination pagination) {
        return PageRequest.of(pagination.page(), pagination.perPage(), of(pagination.order()));
    }

    static Sort of(final Pagination.Order order) {

        if (order == null)
            return Sort.unsorted();

        return Sort.by(of(order.direction()), order.field());
    }

    static Direction of(final Pagination.Order.Direction direction) {
        return Direction.fromString(direction.name());
    }

    static Filter.Group of(final String source, List<? extends Filter.Field> acceptableFields) {

        final var splitedFilters = List.of(source.split("\\|"));

        final var iterator = splitedFilters.iterator();
        if (!iterator.hasNext())
            return new Filter.Group(Filter.Operator.AND, List.of());

        final var filters = new ArrayList<Filter>();

        String next = iterator.next();
        final Filter.Operator operator = Filter.Operator.of(next).orElseThrow();// TODO exception

        while (iterator.hasNext()) {
            next = iterator.next();

            final Map<String, String> map = next == null ? Map.of()
                    : List.of(next.split(";"))
                            .stream()
                            .map(s -> s.split("="))
                            .collect(Collectors.toMap(s -> getSafeArrayElement(s, 0), s -> getSafeArrayElement(s, 1)));

            final var field = acceptableFields
                    .stream()
                    .filter(f -> f.accepts(map.get("field")))
                    .findFirst()
                    .orElseThrow();

            filters.add(
                    new Filter(
                            field,
                            map.get("value"),
                            map.get("valueToCompare"),
                            Filter.Type.of(map.get("type")).orElse(null)));

        }

        return new Filter.Group(operator, filters);

    }

    static Filter ofOld(final String source, List<? extends Filter.Field> fields) {

        final Map<String, String> map = source == null ? Map.of()
                : List.of(source.split(";"))
                        .stream()
                        .map(s -> s.split("="))
                        .collect(Collectors.toMap(s -> getSafeArrayElement(s, 0), s -> getSafeArrayElement(s, 1)));

        return new Filter(
                fields.stream().filter(f -> f.getFieldName().equals(map.get("field"))).findFirst().orElse(null),
                map.get("value"),
                map.get("valueToCompare"),
                Filter.Type.of(map.get("type")).orElse(null));
    }

    private static String getSafeArrayElement(String[] array, int index) {
        return index >= 0 && index < array.length ? array[index] : "";
    }

}
