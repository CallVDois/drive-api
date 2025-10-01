package com.callv2.drive.infrastructure.filter;

import java.util.List;
import java.util.Objects;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.callv2.drive.domain.pagination.Filter;

@Component
public class FilterService {

    private final List<SpecificationFilter> filters;

    public FilterService(final List<SpecificationFilter> filters) {
        this.filters = List.copyOf(Objects.requireNonNull(filters));
    }

    public <T> Specification<T> build(
            final Class<T> entityClass,
            final Filter.Operator groupOperator,
            final List<Filter.Group> filterGroups) {

        return switch (groupOperator) {
            case AND -> andSpecifications(filterGroups
                    .stream()
                    .map(group -> buildGroupSpecification(entityClass, group))
                    .toList());
            case OR -> orSpecifications(filterGroups
                    .stream()
                    .map(group -> buildGroupSpecification(entityClass, group))
                    .toList());
        };

    }

    private <T> Specification<T> buildGroupSpecification(
            final Class<T> entityClass,
            final Filter.Group group) {

        final Specification<T> groupSpecification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        final var elementsIterator = group.elements().iterator();

        if (!elementsIterator.hasNext())
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        final var firstElement = elementsIterator.next();
        groupSpecification.and(buildSpecification(entityClass, firstElement.filter()));

        elementsIterator
                .forEachRemaining(nextElement -> {

                    switch (nextElement.operator()) {
                        case AND -> {
                            groupSpecification.and(buildSpecification(entityClass, nextElement.filter()));
                        }
                        case OR -> {
                            groupSpecification.or(buildSpecification(entityClass, nextElement.filter()));
                        }
                    }

                });

        return groupSpecification;

    }

    private <T> Specification<T> buildElementSpecification(
            final Class<T> entityClass,
            final Specification<T> groupSpecification,
            final Filter.Group.Element element) {

        return switch (element.operator()) {
            case AND -> groupSpecification.and(buildSpecification(entityClass, element.filter()));
            case OR -> groupSpecification.or(buildSpecification(entityClass, element.filter()));
        };

    }

    private <T> Specification<T> buildSpecification(
            final Class<T> entityClass,
            final Filter filter) {
        final var specification = filters.stream()
                .filter(f -> f.filterType().equals(filter.type()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Filter not found"));

        return specification.buildSpecification(filter);
    }

    private static <T> Specification<T> orSpecifications(final List<Specification<T>> specifications) {
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(Specification::or)
                .orElse(null);
    }

    private static <T> Specification<T> andSpecifications(final List<Specification<T>> specifications) {
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(Specification::and)
                .orElse(null);
    }
}
