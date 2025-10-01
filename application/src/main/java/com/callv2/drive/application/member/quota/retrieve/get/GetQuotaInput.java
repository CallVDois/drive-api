package com.callv2.drive.application.member.quota.retrieve.get;

import java.util.UUID;

public record GetQuotaInput(UUID memberId) {

    public static GetQuotaInput of(final UUID memberId) {
        return new GetQuotaInput(memberId);
    }

}
