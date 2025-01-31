package com.callv2.drive.application.member.quota.retrieve.list;

public record QuotaListOutput(String memberId, Long amount) {

    public static QuotaListOutput of(String memberId, Long amount) {
        return new QuotaListOutput(memberId, amount);
    }

}
