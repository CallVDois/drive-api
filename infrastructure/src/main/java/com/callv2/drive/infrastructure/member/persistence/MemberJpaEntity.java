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
    private Long quotaInBytes;

    @Column(nullable = false)
    private Long quotaAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuotaUnit quotaUnit;

    private Long quotaRequestInBytes;

    private Long quotaRequestAmount;

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
            final Long quotaInBytes,
            final Long quotaAmmount,
            final QuotaUnit quotaUnit,
            final Long quotaRequestInBytes,
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
        this.quotaInBytes = quotaInBytes;
        this.quotaAmount = quotaAmmount;
        this.quotaUnit = quotaUnit;
        this.quotaRequestInBytes = quotaRequestInBytes;
        this.quotaRequestAmount = quotaRequestAmmount;
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
        if (getQuotaRequestAmount() != null && getQuotaRequestUnit() != null && getQuotaRequestedAt() != null)
            quotaRequest = QuotaRequest.of(
                    Quota.of(getQuotaRequestAmount(), getQuotaRequestUnit()),
                    getQuotaRequestedAt());

        return Member.with(
                MemberID.of(getId()),
                Username.of(getUsername()),
                Nickname.of(getNickname()),
                Quota.of(getQuotaAmount(), getQuotaUnit()),
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
                member.getQuota().sizeInBytes(),
                member.getQuota().amount(),
                member.getQuota().unit(),
                member.getQuotaRequest().map(QuotaRequest::quota).map(Quota::sizeInBytes).orElse(null),
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

    public Long getQuotaInBytes() {
        return quotaInBytes;
    }

    public void setQuotaInBytes(Long quotaInBytes) {
        this.quotaInBytes = quotaInBytes;
    }

    public Long getQuotaAmount() {
        return quotaAmount;
    }

    public void setQuotaAmount(Long quotaAmmount) {
        this.quotaAmount = quotaAmmount;
    }

    public QuotaUnit getQuotaUnit() {
        return quotaUnit;
    }

    public void setQuotaUnit(QuotaUnit quotaUnit) {
        this.quotaUnit = quotaUnit;
    }

    public Long getQuotaRequestInBytes() {
        return quotaRequestInBytes;
    }

    public void setQuotaRequestInBytes(Long quotaRequestInBytes) {
        this.quotaRequestInBytes = quotaRequestInBytes;
    }

    public Long getQuotaRequestAmount() {
        return quotaRequestAmount;
    }

    public void setQuotaRequestAmount(Long quotaRequestAmmount) {
        this.quotaRequestAmount = quotaRequestAmmount;
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
