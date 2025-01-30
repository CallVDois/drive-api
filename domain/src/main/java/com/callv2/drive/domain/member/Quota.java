package com.callv2.drive.domain.member;

import java.util.Objects;

import com.callv2.drive.domain.validation.Error;
import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.validation.ValidationHandler;

public record Quota(long amount, QuotaUnit unit) implements ValueObject {

    public static Quota of(long amount, QuotaUnit unit) {
        return new Quota(amount, unit);
    }

    @Override
    public void validate(ValidationHandler aHandler) {
        if (Objects.isNull(this.amount))
            aHandler.append(new Error("'amount' should not be null"));

        if (!Objects.isNull(this.amount) && this.amount < 0)
            aHandler.append(new Error("'amount' should be greater than 0"));

        if (Objects.isNull(this.unit))
            aHandler.append(new Error("'unit' should not be null"));
    }

}
