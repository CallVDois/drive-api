package com.callv2.drive.domain.validation.handler;

import java.util.ArrayList;
import java.util.List;

import com.callv2.drive.domain.exception.DomainException;
import com.callv2.drive.domain.validation.Error;
import com.callv2.drive.domain.validation.ValidationHandler;

public class Notification implements ValidationHandler {

    private final List<Error> errors;

    private Notification(final List<Error> errors) {
        this.errors = errors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Throwable t) {
        return new Notification(new ArrayList<>()).append(new Error(t.getMessage()));
    }

    public static Notification create(final Error anError) {
        return new Notification(new ArrayList<>()).append(anError);
    }

    @Override
    public Notification append(Error anError) {
        this.errors.add(anError);
        return this;
    }

    @Override
    public Notification append(ValidationHandler anHandler) {
        this.errors.addAll(anHandler.getErrors());
        return this;
    }

    @Override
    public <T> T valdiate(Validation<T> aValidation) {
        try {
            return aValidation.validate();
        } catch (DomainException e) {
            this.errors.addAll(e.getErrors());
        } catch (Throwable e) {
            this.errors.add(new Error(e.getMessage()));
        }

        return null;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }

}
