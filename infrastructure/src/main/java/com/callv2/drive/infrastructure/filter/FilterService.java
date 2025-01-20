package com.callv2.drive.infrastructure.filter;

import java.util.List;
import java.util.Objects;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.callv2.drive.domain.pagination.SearchQuery;
import com.callv2.drive.domain.pagination.SearchQuery.FilterMethod;

@Component
public class FilterService {

    private final List<SpecificationFilter> filters;

    public FilterService(final List<SpecificationFilter> filters) {
        this.filters = List.copyOf(Objects.requireNonNull(filters));
    }

    public <T> Specification<T> buildSpecification(
            final Class<T> entityClass,
            final SearchQuery.FilterMethod filterMethod,
            final List<SearchQuery.Filter> filters) {

        if (filterMethod.equals(FilterMethod.AND))
            return Specification.where(andSpecifications(buildSpecifications(entityClass, filters)));

        if (filterMethod.equals(FilterMethod.OR))
            return Specification.where(orSpecifications(buildSpecifications(entityClass, filters)));

        return Specification.where(andSpecifications(buildSpecifications(entityClass, filters)));
    }

    private <T> List<Specification<T>> buildSpecifications(Class<T> entityClass,
            final List<SearchQuery.Filter> filters) {
        if (filters == null)
            return List.of();

        return filters.stream()
                .map(filter -> buildSpecification(entityClass, filter))
                .toList();
    }

    private <T> Specification<T> buildSpecification(Class<T> entityClass,
            final SearchQuery.Filter filter) {
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
