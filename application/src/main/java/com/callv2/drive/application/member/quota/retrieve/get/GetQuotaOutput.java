package com.callv2.drive.application.member.quota.retrieve.get;

public record GetQuotaOutput(Long used, Long total, Long available) {

    public static GetQuotaOutput from(final Long used, final Long total, final Long available) {
        return new GetQuotaOutput(used, total, available);
    }

}
