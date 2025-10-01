package com.callv2.drive.infrastructure.member.model;

import java.util.UUID;

public record MemberQuotaListResponse(UUID memberId, String username, Long total) {

}
