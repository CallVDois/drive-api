package com.callv2.drive.application.member.quota.retrieve.get;

public record GetQuotaInput(String id) {

    public static GetQuotaInput of(String id) {
        return new GetQuotaInput(id);
    }

}
