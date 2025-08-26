package com.callv2.drive.application.member.quota.retrieve.get;

import com.callv2.drive.domain.member.Member;

public record GetQuotaOutput(String memberId, String username, Long used, Long total, Long available) {

    public static GetQuotaOutput from(final Member member, final Long usedQuotaInBytes) {
        return new GetQuotaOutput(
                member.getId().getValue(),
                member.getUsername().value(),
                usedQuotaInBytes,
                member.getQuota().sizeInBytes(),
                member.getQuota().sizeInBytes() - usedQuotaInBytes);
    }

}
