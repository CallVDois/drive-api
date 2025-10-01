package com.callv2.drive.infrastructure.member.model;

import java.util.UUID;

public record MemberQuotaResponse(UUID memberId, String username, Long used, Long total, Long available) {

}
