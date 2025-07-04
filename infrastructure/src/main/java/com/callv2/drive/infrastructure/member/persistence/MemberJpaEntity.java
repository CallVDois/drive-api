package com.callv2.drive.infrastructure.member.persistence;

import java.time.Instant;

import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.member.Nickname;
import com.callv2.drive.domain.member.Quota;
import com.callv2.drive.domain.member.QuotaRequest;
import com.callv2.drive.domain.member.QuotaUnit;
import com.callv2.drive.domain.member.Username;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "Member")
@Table(name = "members")
public class MemberJpaEntity {

    @Id
    private String id;

    private String username;

    private String nickname;

    @Column(nullable = false)
    private Long quotaAmmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuotaUnit quotaUnit;

    private Long quotaRequestAmmount;

    @Enumerated(EnumType.STRING)
    private QuotaUnit quotaRequestUnit;

    private Instant quotaRequestedAt;

    @Column(nullable = false)
    private Boolean hasSystemAccess;

    private Instant createdAt;

    private Instant updatedAt;

    private Long synchronizedVersion;

    public MemberJpaEntity(
            final String id,
            final String username,
            final String nickname,
            final Long quotaAmmount,
            final QuotaUnit quotaUnit,
            final Long quotaRequestAmmount,
            final QuotaUnit quotaRequestUnit,
            final Instant quotaRequestedAt,
            final Boolean hasSystemAccess,
            final Instant createdAt,
            final Instant updatedAt,
            final Long synchronizedVersion) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.quotaAmmount = quotaAmmount;
        this.quotaUnit = quotaUnit;
        this.quotaRequestAmmount = quotaRequestAmmount;
        this.quotaRequestUnit = quotaRequestUnit;
        this.quotaRequestedAt = quotaRequestedAt;
        this.hasSystemAccess = hasSystemAccess == null ? false : hasSystemAccess;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.synchronizedVersion = synchronizedVersion;
    }

    public MemberJpaEntity() {
    }

    public Member toDomain() {

        QuotaRequest quotaRequest = null;
        if (getQuotaRequestAmmount() != null && getQuotaRequestUnit() != null && getQuotaRequestedAt() != null)
            quotaRequest = QuotaRequest.of(
                    Quota.of(getQuotaRequestAmmount(), getQuotaRequestUnit()),
                    getQuotaRequestedAt());

        return Member.with(
                MemberID.of(getId()),
                Username.of(getUsername()),
                Nickname.of(getNickname()),
                Quota.of(getQuotaAmmount(), getQuotaUnit()),
                quotaRequest,
                getHasSystemAccess(),
                getCreatedAt(),
                getUpdatedAt(),
                getSynchronizedVersion());
    }

    public static MemberJpaEntity fromDomain(final Member member) {
        return new MemberJpaEntity(
                member.getId().getValue(),
                member.getUsername().value(),
                member.getNickname().value(),
                member.getQuota().amount(),
                member.getQuota().unit(),
                member.getQuotaRequest().map(QuotaRequest::quota).map(Quota::amount).orElse(null),
                member.getQuotaRequest().map(QuotaRequest::quota).map(Quota::unit).orElse(null),
                member.getQuotaRequest().map(QuotaRequest::requesteddAt).orElse(null),
                member.hasSystemAccess(),
                member.getCreatedAt(),
                member.getUpdatedAt(),
                member.getSynchronizedVersion());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getQuotaAmmount() {
        return quotaAmmount;
    }

    public void setQuotaAmmount(Long quotaAmmount) {
        this.quotaAmmount = quotaAmmount;
    }

    public QuotaUnit getQuotaUnit() {
        return quotaUnit;
    }

    public void setQuotaUnit(QuotaUnit quotaUnit) {
        this.quotaUnit = quotaUnit;
    }

    public Long getQuotaRequestAmmount() {
        return quotaRequestAmmount;
    }

    public void setQuotaRequestAmmount(Long quotaRequestAmmount) {
        this.quotaRequestAmmount = quotaRequestAmmount;
    }

    public QuotaUnit getQuotaRequestUnit() {
        return quotaRequestUnit;
    }

    public void setQuotaRequestUnit(QuotaUnit quotaRequestUnit) {
        this.quotaRequestUnit = quotaRequestUnit;
    }

    public Instant getQuotaRequestedAt() {
        return quotaRequestedAt;
    }

    public void setQuotaRequestedAt(Instant quotaRequestedAt) {
        this.quotaRequestedAt = quotaRequestedAt;
    }

    public Boolean getHasSystemAccess() {
        return hasSystemAccess;
    }

    public void setHasSystemAccess(Boolean hasSystemAccess) {
        this.hasSystemAccess = hasSystemAccess;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getSynchronizedVersion() {
        return synchronizedVersion;
    }

    public void setSynchronizedVersion(Long synchronizedVersion) {
        this.synchronizedVersion = synchronizedVersion;
    }

}
