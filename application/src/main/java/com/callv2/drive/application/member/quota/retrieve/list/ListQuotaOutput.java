package com.callv2.drive.application.member.quota.retrieve.list;

import com.callv2.drive.domain.member.Member;

public record ListQuotaOutput(String memberId, String username, Long total) {

    public static ListQuotaOutput of(final Member member) {
        return new ListQuotaOutput(
                member.getId().getValue(),
                member.getUsername().value(),
                member.getQuota().sizeInBytes());
    }

}
