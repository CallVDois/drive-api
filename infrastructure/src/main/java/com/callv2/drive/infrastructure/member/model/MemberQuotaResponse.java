package com.callv2.drive.infrastructure.member.model;

public record MemberQuotaResponse(String memberId, String username, Long used, Long total, Long available) {

}
