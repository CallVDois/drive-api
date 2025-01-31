package com.callv2.drive.application.member.quota.request.approve;

public record ApproveRequestQuotaInput(String memberId, boolean approved) {

    public static ApproveRequestQuotaInput of(String memberId, boolean approved) {
        return new ApproveRequestQuotaInput(memberId, approved);
    }

}
