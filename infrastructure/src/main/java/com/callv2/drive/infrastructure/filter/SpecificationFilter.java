package com.callv2.drive.infrastructure.filter;

import org.springframework.data.jpa.domain.Specification;

import com.callv2.drive.domain.pagination.SearchQuery;

public interface SpecificationFilter {

    SearchQuery.Filter.Type filterType();

    <T, V extends Comparable<V>> Specification<T> buildSpecification(SearchQuery.Filter<V> filter);
}
