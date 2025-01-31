package com.callv2.drive.application.member.quota.request.create;

import com.callv2.drive.domain.member.QuotaUnit;

public record CreateRequestQuotaInput(String memberId, Long ammount, QuotaUnit unit) {

    public static CreateRequestQuotaInput of(String memberId, Long amount, QuotaUnit unit) {
        return new CreateRequestQuotaInput(memberId, amount, unit);
    }

}
