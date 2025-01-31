package com.callv2.drive.domain.member;

import java.time.Instant;

import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.validation.Error;
import com.callv2.drive.domain.validation.ValidationHandler;

public record QuotaRequest(Quota quota, Instant requesteddAt) implements ValueObject {

    public static QuotaRequest of(Quota quota, Instant requestedAt) {
        return new QuotaRequest(quota, requestedAt);
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (quota == null)
            handler.append(new Error("'quota' should not be null"));

        if (requesteddAt == null)
            handler.append(new Error("'requestedAt' should not be null"));

        if (quota != null)
            quota.validate(handler);
    }

}
