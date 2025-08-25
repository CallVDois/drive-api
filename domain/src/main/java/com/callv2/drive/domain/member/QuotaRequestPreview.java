package com.callv2.drive.domain.member;

import java.time.Instant;

public record QuotaRequestPreview(
        String memberId,
        String memberUsername,
        String memberNickname,
        long quotaAmount,
        QuotaUnit quotaUnit,
        Instant quotaRequestedAt) {

}
