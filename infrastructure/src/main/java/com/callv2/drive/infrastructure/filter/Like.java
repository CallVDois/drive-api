package com.callv2.drive.infrastructure.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.callv2.drive.domain.pagination.SearchQuery;
import com.callv2.drive.infrastructure.converter.Caster;

@Component
public class Like extends SpecificationFilter {

    public Like(final Caster caster) {
        super(caster);
    }

    @Override
    public SearchQuery.Filter.Type filterType() {
        return SearchQuery.Filter.Type.LIKE;
    }

    @Override
    public <T> Specification<T> buildSpecification(SearchQuery.Filter filter) {

        validateFilter(filter);

        return (root, query, criteriaBuilder) -> criteriaBuilder
                .like(root.get(filter.field()), "%" + filter.value() + "%");
    }

    private void validateFilter(final SearchQuery.Filter filter) {
        if (filter.value() == null)
            throw new IllegalArgumentException("Value cannot be null");

        if (!SearchQuery.Filter.Type.LIKE.equals(filter.type()))
            throw new IllegalArgumentException("Filter type must be LIKE");
    }

}
