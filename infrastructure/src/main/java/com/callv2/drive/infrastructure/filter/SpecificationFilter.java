package com.callv2.drive.infrastructure.filter;

import java.util.Objects;

import org.springframework.data.jpa.domain.Specification;

import com.callv2.drive.domain.pagination.Filter;
import com.callv2.drive.infrastructure.converter.Caster;

public abstract class SpecificationFilter {

    private final Caster caster;

    protected SpecificationFilter(final Caster caster) {
        this.caster = Objects.requireNonNull(caster);
    }

    abstract Filter.Type filterType();

    abstract <T> Specification<T> buildSpecification(Filter filter);

    protected <T> T cast(final Object value, final Class<T> clazz) {
        return caster.cast(value, clazz);
    }

}
