package com.callv2.drive.domain.file;

import com.callv2.drive.domain.validation.Error;
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

        if (this.file.getId() == null) {
            this.validationHandler().append(new Error("'id' should not be null"));
            return;
        }

        this.file.getId().validate(this.validationHandler());
    }

    private void validateName() {

        if (this.file.getName() == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        this.file.getName().validate(this.validationHandler());
    }

    private void validateContent() {

        if (this.file.getContent() == null) {
            this.validationHandler().append(new Error("'content' should not be null"));
            return;
        }

        this.file.getContent().validate(this.validationHandler());
    }

}
