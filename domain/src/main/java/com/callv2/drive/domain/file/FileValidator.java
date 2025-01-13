package com.callv2.drive.domain.file;

import com.callv2.drive.domain.validation.ValidationHandler;
import com.callv2.drive.domain.validation.Validator;

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
        this.validateContent();
    }

    private void validateId() {
        this.file.getId().validate(this.validationHandler());
    }

    private void validateName() {
        this.file.getName().validate(this.validationHandler());
    }

    private void validateContent() {
        this.file.getContent().validate(this.validationHandler());
    }

}
