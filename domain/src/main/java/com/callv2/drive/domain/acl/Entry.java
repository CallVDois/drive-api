package com.callv2.drive.domain.acl;

import static java.util.Objects.isNull;

import java.time.Instant;

import com.callv2.drive.domain.Entity;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.ValidationHandler;

public class Entry<P extends Permission<?>> extends Entity<EntryID> {

    private MemberID member;
    private P permission;
    private Instant grantedAt;

    private Entry(
            final EntryID id,
            final MemberID member,
            final P permission,
            final Instant grantedAt) {
        super(id);
        this.member = member;
        this.permission = permission;
        this.grantedAt = grantedAt;
    }

    @Override
    public void validate(final ValidationHandler handler) {
    }

    public static <P extends Permission<?>> Entry<P> with(
            final EntryID id,
            final MemberID member,
            final P permission,
            final Instant grantedAt) {
        return new Entry<>(id, member, permission, grantedAt);
    }

    public static <P extends Permission<?>> Entry<P> create(
            final MemberID member,
            final P permission) {
        return new Entry<>(EntryID.unique(), member, permission, Instant.now());
    }

    public static <P extends Permission<?>> Entry<P> inherit(final Entry<P> entry) {
        return new Entry<>(EntryID.unique(), entry.getMember(), entry.getPermission(), Instant.now());
    }

    public Boolean isEquivalentTo(final Entry<? extends Permission<?>> other) {

        return isNull(other) ? false
                : getMember().equals(other.getMember())
                        && getPermission().equals(other.getPermission());

    }

    public MemberID getMember() {
        return member;
    }

    public P getPermission() {
        return permission;
    }

    public Instant getGrantedAt() {
        return grantedAt;
    }

}
