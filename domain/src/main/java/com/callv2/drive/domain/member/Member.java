package com.callv2.drive.domain.member;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import com.callv2.drive.domain.AggregateRoot;
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

    private Member(
            final MemberID id,
            final Username username,
            final Nickname nickname,
            final Quota quota,
            final QuotaRequest quotaRequest,
            final Instant createdAt,
            final Instant updatedAt) {
        super(id);
        this.username = username;
        this.nickname = nickname;
        this.quota = quota;
        this.quotaRequest = Optional.ofNullable(quotaRequest);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Member create(final MemberID id, final Nickname nickname, final Username username) {

        final Instant now = Instant.now();
        final Quota quota = Quota.of(0, QuotaUnit.BYTE);

        return new Member(id, username, nickname, quota, null, now, now);
    }

    public static Member with(
            final MemberID id,
            final Username username,
            final Nickname nickname,
            final Quota quota,
            final QuotaRequest quotaRequest,
            final Instant createdAt,
            final Instant updatedAt) {
        return new Member(id, username, nickname, quota, quotaRequest, createdAt, updatedAt);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new MemberValidator(this, handler).validate();
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

    public Member changeNickname(final Nickname nickname) {
        final Notification notification = Notification.create();
        Objects.requireNonNull(nickname).validate(notification);
        if (notification.hasError())
            throw ValidationException.with("Change Nickname Error", notification);

        this.nickname = nickname;
        this.updatedAt = Instant.now();
        return this;
    }

    public Member changeUsername(final Username username) {
        final Notification notification = Notification.create();
        Objects.requireNonNull(username).validate(notification);
        if (notification.hasError())
            throw ValidationException.with("Change Username Error", notification);

        this.username = username;
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

}
