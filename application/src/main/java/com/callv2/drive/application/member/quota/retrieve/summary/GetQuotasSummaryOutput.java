package com.callv2.drive.application.member.quota.retrieve.summary;

public record GetQuotasSummaryOutput(
        Long membersCount,
        Long totalAllocatedQuota,
        Long totalUsedQuota,
        Long totalAvailableQuota) {

}
