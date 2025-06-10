package com.callv2.drive.domain.member;

import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.ValidationHandler;
import com.callv2.drive.domain.validation.Validator;

public class MemberValidator extends Validator {

    private final Member member;

    protected MemberValidator(final Member aMember, final ValidationHandler handler) {
        super(handler);
        this.member = aMember;
    }

    @Override
    public void validate() {
        validateId();
        validateUsername();
        validateNickname();
        validateQuota();
        validateQuotaRequest();
    }

    private void validateId() {
        if (this.member.getId() == null) {
            this.validationHandler().append(ValidationError.with("'id' is required"));
            return;
        }

        this.member.getId().validate(this.validationHandler());
    }

    private void validateUsername() {
        if (this.member.getUsername() == null) {
            this.validationHandler().append(ValidationError.with("'username' is required"));
            return;
        }

        this.member.getUsername().validate(this.validationHandler());
    }

    private void validateNickname() {
        if (this.member.getNickname() == null) {
            this.validationHandler().append(ValidationError.with("'nickname' is required"));
            return;
        }

        this.member.getNickname().validate(this.validationHandler());
    }

    private void validateQuota() {
        if (this.member.getQuota() == null) {
            this.validationHandler().append(ValidationError.with("'quota' is required"));
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
