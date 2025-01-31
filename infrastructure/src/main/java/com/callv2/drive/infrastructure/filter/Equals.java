package com.callv2.drive.infrastructure.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.callv2.drive.domain.pagination.Filter;
import com.callv2.drive.infrastructure.converter.Caster;

@Component
public class Equals extends SpecificationFilter {

    public Equals(final Caster caster) {
        super(caster);
    }

    @Override
    public Filter.Type filterType() {
        return Filter.Type.EQUALS;
    }

    @Override
    public <T> Specification<T> buildSpecification(Filter filter) {
        return (root, query, criteriaBuilder) -> {
            final var field = root.get(filter.field());
            return criteriaBuilder.equal(field, cast(filter.value(), field.getJavaType()));
        };
    }

}
