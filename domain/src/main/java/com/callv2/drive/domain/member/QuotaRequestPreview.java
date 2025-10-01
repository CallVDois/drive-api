package com.callv2.drive.domain.member;

import java.time.Instant;
import java.util.UUID;

public record QuotaRequestPreview(
        UUID memberId,
        String memberUsername,
        String memberNickname,
        long quotaAmount,
        QuotaUnit quotaUnit,
        Instant quotaRequestedAt) {

}
