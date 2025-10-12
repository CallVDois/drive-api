package com.callv2.drive.application.member.usecase.quota.request.create;

import java.util.UUID;

import com.callv2.drive.domain.member.QuotaUnit;

public record CreateRequestQuotaInput(UUID memberId, Long amount, QuotaUnit unit) {

    public static CreateRequestQuotaInput of(UUID memberId, Long amount, QuotaUnit unit) {
        return new CreateRequestQuotaInput(memberId, amount, unit);
    }

}
