package com.callv2.drive.domain;

import java.util.Objects;

import com.callv2.drive.domain.validation.ValidationHandler;

public abstract class Entity<ID extends Identifier<?>> {

    protected final ID id;

    protected Entity(final ID id) {
        this.id = Objects.requireNonNull(id, "'id' should not be null");
    }

    public abstract void validate(ValidationHandler handler);

    public ID getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Entity<?> other = (Entity<?>) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
