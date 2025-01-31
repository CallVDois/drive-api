package com.callv2.drive.domain.member;

import com.callv2.drive.domain.validation.Error;
import com.callv2.drive.domain.validation.ValidationHandler;
import com.callv2.drive.domain.validation.Validator;

public class MemberValidator extends Validator {

    private final Member member;

    protected MemberValidator(final Member aMember, final ValidationHandler aHandler) {
        super(aHandler);
        this.member = aMember;
    }

    @Override
    public void validate() {
        validateId();
        validateQuota();
        validateQuotaRequest();
    }

    private void validateId() {
        if (this.member.getId() == null) {
            this.validationHandler().append(Error.with("'id' is required"));
            return;
        }

        this.member.getId().validate(this.validationHandler());
    }

    private void validateQuota() {
        if (this.member.getQuota() == null) {
            this.validationHandler().append(Error.with("'quota' is required"));
            return;
        }

        this.member.getQuota().validate(this.validationHandler());
    }

    private void validateQuotaRequest() {
        if (this.member.getQuotaRequest().isEmpty())
            return;

        this.member.getQuotaRequest().get().validate(this.validationHandler());
    }

}
