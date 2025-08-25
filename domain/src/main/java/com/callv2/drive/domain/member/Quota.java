package com.callv2.drive.domain.member;

import java.util.Objects;

import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.validation.ValidationHandler;

public record Quota(long amount, QuotaUnit unit) implements ValueObject {

    public static Quota of(long amount, QuotaUnit unit) {
        return new Quota(amount, unit);
    }

    public long sizeInBytes() {
        return this.unit != null ? this.unit.toBytes(this.amount) : 0L;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (Objects.isNull(this.amount))
            handler.append(new ValidationError("'amount' should not be null"));

        if (!Objects.isNull(this.amount) && this.amount < 0)
            handler.append(new ValidationError("'amount' should be greater than 0"));

        if (Objects.isNull(this.unit))
            handler.append(new ValidationError("'unit' should not be null"));
    }

}
