package com.callv2.drive.infrastructure.filter;

import java.util.List;
import java.util.Objects;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.callv2.drive.domain.pagination.SearchQuery;

@Component
public class SpecificationFilterService {

    private final List<SpecificationFilter> filters;

    public SpecificationFilterService(final List<SpecificationFilter> filters) {
        this.filters = List.copyOf(Objects.requireNonNull(filters));
    }

    public <T> List<Specification<T>> buildSpecifications(Class<T> entityClass,
            final List<SearchQuery.Filter<?>> filters) {
        if (filters == null)
            return List.of();

        return filters.stream()
                .map(filter -> buildSpecification(entityClass, filter))
                .toList();
    }

    public <T> Specification<T> buildSpecification(Class<T> entityClass,
            final SearchQuery.Filter<?> filter) {
        final var specification = filters.stream()
                .filter(f -> f.filterType().equals(filter.type()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Filter not found"));

        return specification.buildSpecification(filter);
    }

    public static <T> Specification<T> orSpecifications(final List<Specification<T>> specifications) {
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(Specification::or)
                .orElse(null);
    }
}
