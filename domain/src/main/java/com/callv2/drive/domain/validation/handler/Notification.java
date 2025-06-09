package com.callv2.drive.domain.validation.handler;

import java.util.ArrayList;
import java.util.List;

import com.callv2.drive.domain.exception.DomainException;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.ValidationHandler;

public class Notification implements ValidationHandler {

    private final List<ValidationError> errors;

    private Notification(final List<ValidationError> errors) {
        this.errors = errors == null ? new ArrayList<>() : errors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final ValidationError error) {
        return new Notification(new ArrayList<>()).append(error);
    }

    @Override
    public Notification append(final ValidationError error) {
        this.errors.add(error);
        return this;
    }

    @Override
    public Notification append(final ValidationHandler handler) {
        this.errors.addAll(handler.getErrors());
        return this;
    }

    @Override
    public <T> T valdiate(final Validation<T> validation) {
        try {
            return validation.validate();
        } catch (DomainException e) {
            this.errors.addAll(e.getErrors().stream().map(ValidationError::fromDomainError).toList());
        } catch (Throwable e) {
            this.errors.add(new ValidationError(e.getMessage()));
        }

        return null;
    }

    @Override
    public List<ValidationError> getErrors() {
        return this.errors == null ? List.of() : List.copyOf(this.errors);
    }

}
