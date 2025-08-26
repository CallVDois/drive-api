package com.callv2.drive.infrastructure.member.model;

public record QuotaSummaryResponse(
        Long membersCount,
        Long totalAllocatedQuota,
        Long totalUsedQuota,
        Long totalAvailableQuota) {

}
