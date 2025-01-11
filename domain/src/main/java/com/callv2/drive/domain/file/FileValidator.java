package com.callv2.drive.domain.file;

import com.callv2.drive.domain.validation.ValidationHandler;
import com.callv2.drive.domain.validation.Validator;
import com.callv2.drive.domain.validation.Error;

public class FileValidator extends Validator {

    private final File file;

    protected FileValidator(final File aFile, final ValidationHandler aHandler) {
        super(aHandler);
        this.file = aFile;
    }

    @Override
    public void validate() {
        this.validateId();
        this.validateName();
        this.validateContentType();
    }

    private void validateId() {
        this.file.getId().validate(this.validationHandler());
    }

    private void validateName() {
        this.file.getName().validate(this.validationHandler());
    }

    private void validateContentType() {

        final String value = this.file.getContentType();

        if (value == null) {
            this.validationHandler().append(new Error("'contentType' cannot be null."));
            return;
        }

        if (value.trim().isEmpty()) {
            this.validationHandler().append(new Error("'contentType' cannot be empty."));
            return;
        }
    }

}
