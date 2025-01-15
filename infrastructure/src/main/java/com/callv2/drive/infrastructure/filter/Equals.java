package com.callv2.drive.infrastructure.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.callv2.drive.domain.pagination.SearchQuery;

@Component
public class Equals implements SpecificationFilter {

    @Override
    public SearchQuery.Filter.Type filterType() {
        return SearchQuery.Filter.Type.EQUALS;
    }

    @Override
    public <T, V extends Comparable<V>> Specification<T> buildSpecification(final SearchQuery.Filter<V> filter) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(filter.field()), filter.value());
    }

}
