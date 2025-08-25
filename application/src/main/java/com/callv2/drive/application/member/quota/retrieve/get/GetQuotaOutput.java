package com.callv2.drive.application.member.quota.retrieve.get;

public record GetQuotaOutput(String memberId, Long used, Long total, Long available) {

    public static GetQuotaOutput from(final String memberId, final Long used, final Long total, final Long available) {
        return new GetQuotaOutput(memberId, used, total, available);
    }

}
