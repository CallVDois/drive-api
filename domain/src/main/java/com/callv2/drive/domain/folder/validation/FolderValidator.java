package com.callv2.drive.domain.folder.validation;

import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.validation.ValidationHandler;
import com.callv2.drive.domain.validation.Validator;

public class FolderValidator extends Validator {

    private final Folder folder;

    public FolderValidator(final Folder folder, final ValidationHandler handler) {
        super(handler);
        this.folder = folder;
    }

    @Override
    public void validate() {
        this.validateId();
        this.validateName();
    }

    private void validateId() {
        this.folder.getId().validate(this.validationHandler());
    }

    private void validateName() {
        this.folder.getName().validate(this.validationHandler());
    }

}
