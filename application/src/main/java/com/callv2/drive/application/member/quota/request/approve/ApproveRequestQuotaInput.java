package com.callv2.drive.application.member.quota.request.approve;

import java.util.UUID;

public record ApproveRequestQuotaInput(UUID memberId, boolean approved) {

    public static ApproveRequestQuotaInput of(UUID memberId, boolean approved) {
        return new ApproveRequestQuotaInput(memberId, approved);
    }

}
