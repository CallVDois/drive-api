package com.callv2.drive.infrastructure.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.callv2.drive.domain.pagination.SearchQuery;

@Component
public class Between implements SpecificationFilter {

    @Override
    public SearchQuery.Filter.Type filterType() {
        return SearchQuery.Filter.Type.BETWEEN;
    }

    @Override
    public <T, V extends Comparable<V>> Specification<T> buildSpecification(final SearchQuery.Filter<V> filter) {

        validateFilter(filter);

        return (root, query, criteriaBuilder) -> criteriaBuilder
                .between(root.get(filter.field()), filter.value(), filter.valueToCompare());
    }

    private void validateFilter(final SearchQuery.Filter<?> filter) {
        if (filter.value() == null || filter.valueToCompare() == null)
            throw new IllegalArgumentException("Value cannot be null");

        if (!SearchQuery.Filter.Type.BETWEEN.equals(filter.type()))
            throw new IllegalArgumentException("Filter type must be BETWEEN");
    }

}
