package com.callv2.drive.domain.access;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.callv2.drive.domain.AggregateRoot;
import com.callv2.drive.domain.event.Event;
import com.callv2.drive.domain.event.EventSource;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.ValidationHandler;

public class Acl extends AggregateRoot<AclID> implements EventSource {

    private Queue<Event<?>> events;

    private final Resource<?> resource;
    private Set<Entry> directEntries;
    private Set<Entry> inheritedEntries;

    private final Instant createdAt;
    private Instant updatedAt;

    public Acl(
            final AclID id,
            final Resource<?> resource,
            final Set<Entry> directEntries,
            final Set<Entry> inheritedEntries,
            final Instant createdAt,
            final Instant updatedAt) {
        super(id);
        this.resource = resource;
        this.directEntries = nonNull(directEntries) ? new HashSet<>(directEntries) : new HashSet<>();
        this.inheritedEntries = nonNull(inheritedEntries) ? new HashSet<>(inheritedEntries) : new HashSet<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        this.events = new LinkedList<>();
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(resource))
            handler.append(ValidationError.with("'resource' cannot be null"));

        if (nonNull(resource))
            resource.validate(handler);

    }

    @Override
    public Optional<Event<?>> nextEvent() {
        return Optional.ofNullable(this.events.poll());
    }

    public static Acl with(
            final AclID id,
            final Resource<?> resource,
            final Set<Entry> directEntries,
            final Set<Entry> inheritedEntries,
            final Instant createdAt,
            final Instant updatedAt) {
        return new Acl(id, resource, directEntries, inheritedEntries, createdAt, updatedAt);
    }

    public static Acl create(final Resource<?> resource) {
        Instant now = Instant.now();
        return new Acl(
                AclID.unique(),
                resource,
                null,
                null,
                now,
                now);
    }

    public Acl createInherited(final Resource<?> resource) {
        final Instant now = Instant.now();

        final Set<Entry> inheritedEntries = Stream
                .concat(this.directEntries.stream(), this.inheritedEntries.stream())
                .collect(Collectors.toSet());

        return new Acl(
                AclID.unique(),
                resource,
                null,
                inheritedEntries,
                now,
                now);

    }

    public Acl inheritFrom(final Acl parentAcl) {
        if (isNull(parentAcl))
            throw new IllegalArgumentException("'parentAcl' should not be null");

        final Instant now = Instant.now();

        final Set<Entry> inheritedEntries = Stream
                .concat(parentAcl.directEntries.stream(), parentAcl.inheritedEntries.stream())
                .collect(Collectors.toSet());

        this.inheritedEntries = inheritedEntries;
        this.updatedAt = now;

        this.events.add(AclUpdatedEvent.create(this));

        return this;
    }

    public Acl grantTotal(final MemberID member) {
        return this.grantEntry(member, AccessPermission.mostPrivileged(), SharePermission.mostPrivileged());
    }

    public Acl grantEntry(
            MemberID member,
            AccessPermission accessPermission,
            SharePermission sharePermission) {

        if (isNull(member))
            throw new IllegalArgumentException("'member' should not be null");
        if (isNull(accessPermission))
            throw new IllegalArgumentException("'accessPermission' should not be null");
        if (isNull(sharePermission))
            throw new IllegalArgumentException("'sharePermission' should not be null");

        final Entry entry = Entry.create(member, accessPermission, sharePermission);

        if (this.directEntries.stream().anyMatch(e -> e.isEquivalentTo(entry)))
            return this;

        this.directEntries.add(entry);
        this.updatedAt = Instant.now();

        this.events.add(AclUpdatedEvent.create(this));

        return this;
    }

    public Optional<AccessPermission> effectiveAccessPermission(final MemberID member) {

        return Stream.concat(directEntries.stream(), inheritedEntries.stream())
                .filter(entry -> entry.member().equals(member))
                .map(Entry::accessPermission)
                .min((e1, e2) -> e1.getLevel().compareTo(e2.getLevel()));

    }

    public Optional<SharePermission> effectiveSharePermission(final MemberID member) {

        return Stream.concat(directEntries.stream(), inheritedEntries.stream())
                .filter(entry -> entry.member().equals(member))
                .map(Entry::sharePermission)
                .min((e1, e2) -> e1.getLevel().compareTo(e2.getLevel()));

    }

    public Resource<?> getResource() {
        return resource;
    }

    public Set<Entry> getDirectEntries() {
        return Set.copyOf(directEntries);
    }

    public Set<Entry> getInheritedEntries() {
        return Set.copyOf(inheritedEntries);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}
