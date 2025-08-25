package com.callv2.drive.application.member.quota.request.list;

import java.time.Instant;

import com.callv2.drive.domain.member.QuotaRequestPreview;
import com.callv2.drive.domain.member.QuotaUnit;

public record ListRequestQuotaOutput(
        String memberId,
        String memberUsername,
        String memberNickname,
        long quotaAmount,
        QuotaUnit quotaUnit,
        Instant quotaRequestedAt) {

    public static ListRequestQuotaOutput from(final QuotaRequestPreview quotaRequestPreview) {
        return new ListRequestQuotaOutput(
                quotaRequestPreview.memberId(),
                quotaRequestPreview.memberUsername(),
                quotaRequestPreview.memberNickname(),
                quotaRequestPreview.quotaAmount(),
                quotaRequestPreview.quotaUnit(),
                quotaRequestPreview.quotaRequestedAt());
    }

}
