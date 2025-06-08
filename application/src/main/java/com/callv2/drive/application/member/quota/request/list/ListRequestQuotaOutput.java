package com.callv2.drive.application.member.quota.request.list;

import java.time.Instant;

import com.callv2.drive.domain.member.QuotaRequestPreview;
import com.callv2.drive.domain.member.QuotaUnit;

public record ListRequestQuotaOutput(
        String memberId,
        long amount,
        QuotaUnit unit,
        Instant requestedAt) {

    public static ListRequestQuotaOutput from(final QuotaRequestPreview quotaRequestPreview) {
        return new ListRequestQuotaOutput(
                quotaRequestPreview.memberId(),
                quotaRequestPreview.amount(),
                quotaRequestPreview.unit(),
                quotaRequestPreview.requestedAt());
    }

}
