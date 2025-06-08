package com.callv2.drive.domain.member;

import java.time.Instant;
import java.util.Optional;

import com.callv2.drive.domain.AggregateRoot;
import com.callv2.drive.domain.exception.IdMismatchException;
import com.callv2.drive.domain.exception.SynchronizedVersionOutdatedException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.validation.ValidationHandler;
import com.callv2.drive.domain.validation.handler.Notification;

public class Member extends AggregateRoot<MemberID> {

    private Username username;
    private Nickname nickname;

    private Quota quota;
    private Optional<QuotaRequest> quotaRequest;

    private Instant createdAt;
    private Instant updatedAt;

    private Long synchronizedVersion;

    private Member(
            final MemberID id,
            final Username username,
            final Nickname nickname,
            final Quota quota,
            final QuotaRequest quotaRequest,
            final Instant createdAt,
            final Instant updatedAt,
            final Long version) {
        super(id);
        this.username = username;
        this.nickname = nickname;
        this.quota = quota == null ? Quota.of(0, QuotaUnit.BYTE) : quota;
        this.quotaRequest = Optional.ofNullable(quotaRequest);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        this.synchronizedVersion = version == null ? 0L : version;
    }

    public static Member with(
            final MemberID id,
            final Username username,
            final Nickname nickname,
            final Quota quota,
            final QuotaRequest quotaRequest,
            final Instant createdAt,
            final Instant updatedAt,
            final Long synchronizedVersion) {
        return new Member(id, username, nickname, quota, quotaRequest, createdAt, updatedAt, synchronizedVersion);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new MemberValidator(this, handler).validate();
    }

    public Member synchronize(final Member member) {

        if (!this.id.equals(member.id))
            throw IdMismatchException.with(
                    Member.class,
                    member.id.getValue());

        if (this.synchronizedVersion > member.synchronizedVersion)
            throw SynchronizedVersionOutdatedException
                    .with(
                            Member.class,
                            this.synchronizedVersion,
                            member.synchronizedVersion);

        this.nickname = member.nickname;
        this.username = member.username;

        this.createdAt = member.createdAt;
        this.updatedAt = member.updatedAt;
        this.synchronizedVersion = member.synchronizedVersion;

        return this;
    }

    public Member requestQuota(final Quota quota) {

        final QuotaRequest newQuotaRequest = QuotaRequest.of(quota, Instant.now());

        final Notification notification = Notification.create();
        newQuotaRequest.validate(notification);

        if (notification.hasError())
            throw ValidationException.with("Request Quota Error", notification);

        this.quotaRequest = Optional.ofNullable(newQuotaRequest);
        this.updatedAt = Instant.now();
        return this;
    }

    public Member approveQuotaRequest() {

        if (this.quotaRequest.isEmpty())
            return this;

        this.quota = this.quotaRequest
                .map(QuotaRequest::quota)
                .orElse(Quota.of(0, QuotaUnit.BYTE));

        this.quotaRequest = Optional.empty();

        this.updatedAt = Instant.now();

        return this;
    }

    public Member reproveQuotaRequest() {

        if (this.quotaRequest.isEmpty())
            return this;

        this.quotaRequest = Optional.empty();
        this.updatedAt = Instant.now();

        return this;
    }

    public Username getUsername() {
        return username;
    }

    public Nickname getNickname() {
        return nickname;
    }

    public Quota getQuota() {
        return quota;
    }

    public Optional<QuotaRequest> getQuotaRequest() {
        return quotaRequest;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getSynchronizedVersion() {
        return synchronizedVersion;
    }

}
