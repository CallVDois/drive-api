package com.callv2.drive.infrastructure.member.model;

public record MemberQuotaResponse(String memberId, Long used, Long total, Long available) {

}
