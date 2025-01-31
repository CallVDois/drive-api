package com.callv2.drive.domain.member;

import java.time.Instant;

public record QuotaRequestPreview(
        String memberId,
        long amount,
        QuotaUnit unit,
        Instant requestedAt) {

}
