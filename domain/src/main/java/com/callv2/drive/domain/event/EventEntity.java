package com.callv2.drive.domain.event;

import com.callv2.drive.domain.Entity;
import com.callv2.drive.domain.Identifier;

public record EventEntity(String type, String id) {

    public static <E extends Entity<I>, I extends Identifier<?>> EventEntity of(
            final Class<? extends E> entityClass,
            final I id) {
        return new EventEntity(entityClass.getSimpleName(), id.getStringValue());
    }

    public static <E extends Entity<I>, I extends Identifier<?>> EventEntity of(final E entity) {
        return new EventEntity(entity.getClass().getSimpleName(), entity.getId().getStringValue());
    }

}