package com.callv2.drive.application.member.quota.retrieve.get;

public record GetQuotaInput(String memberId) {

    public static GetQuotaInput of(final String memberId) {
        return new GetQuotaInput(memberId);
    }

}
