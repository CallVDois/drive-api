package com.callv2.drive.application.member.quota.retrieve.list;

import java.util.UUID;

import com.callv2.drive.domain.member.Member;

public record ListQuotaOutput(UUID memberId, String username, Long total) {

    public static ListQuotaOutput of(final Member member) {
        return new ListQuotaOutput(
                member.getId().getValue(),
                member.getUsername().value(),
                member.getQuota().sizeInBytes());
    }

}
