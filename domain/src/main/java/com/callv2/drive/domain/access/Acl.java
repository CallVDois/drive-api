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
import com.callv2.drive.domain.exception.NotAllowedException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.ValidationHandler;
import com.callv2.drive.domain.validation.handler.Notification;

public class Acl extends AggregateRoot<AclID> implements EventSource {

    private final Queue<Event<?>> events;

    private final Resource<?> resource;
    private Set<Entry<?>> directEntries;
    private Set<Entry<?>> inheritedEntries;

    private final Instant createdAt;
    private Instant updatedAt;

    private Acl(
            final AclID id,
            final Resource<?> resource,
            final Set<Entry<?>> directEntries,
            final Set<Entry<?>> inheritedEntries,
            final Instant createdAt,
            final Instant updatedAt,
            final Queue<Event<?>> events) {
        super(id);
        this.resource = resource;
        this.directEntries = nonNull(directEntries) ? new HashSet<>(directEntries) : new HashSet<>();
        this.inheritedEntries = nonNull(inheritedEntries) ? new HashSet<>(inheritedEntries) : new HashSet<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        this.events = nonNull(events) ? new LinkedList<>(events) : new LinkedList<>();
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
            final Set<Entry<?>> directEntries,
            final Set<Entry<?>> inheritedEntries,
            final Instant createdAt,
            final Instant updatedAt,
            final Queue<Event<?>> events) {
        return new Acl(id, resource, directEntries, inheritedEntries, createdAt, updatedAt, events);
    }

    public static Acl create(final Resource<?> resource, final MemberID owner) {

        Instant now = Instant.now();

        final Acl newAcl = new Acl(
                AclID.unique(),
                resource,
                null,
                null,
                now,
                now,
                null)
                .grantTotal(owner);

        return newAcl;
    }

    public Acl createInherited(final Resource<?> resource) {
        final Instant now = Instant.now();

        final Set<Entry<?>> inheritedEntries = Stream
                .concat(this.directEntries.stream(), this.inheritedEntries.stream())
                .collect(Collectors.toSet());

        final Acl newAcl = new Acl(
                AclID.unique(),
                resource,
                null,
                inheritedEntries,
                now,
                now,
                null);

        return newAcl;

    }

    public Acl inheritFrom(final Acl parentAcl) {
        if (isNull(parentAcl))
            throw ValidationException.with(
                    "Could not inherit ACL",
                    ValidationError.with("'parentAcl' should not be null"));

        final Instant now = Instant.now();

        final Set<Entry<?>> inheritedEntries = Stream
                .concat(parentAcl.directEntries.stream(), parentAcl.inheritedEntries.stream())
                .collect(Collectors.toSet());

        this.inheritedEntries = inheritedEntries;
        this.updatedAt = now;

        this.events.add(AclUpdatedEvent.create(this));

        return this;
    }

    public Optional<AccessPermission> effectiveAccessPermission(final MemberID member) {

        if (this.resource.owner().equals(member))
            return Optional.of(AccessPermission.mostPrivileged());

        return Stream.concat(directEntries.stream(), inheritedEntries.stream())
                .filter(entry -> entry.getMember().equals(member))
                .map(Entry::getPermission)
                .filter(AccessPermission.class::isInstance)
                .map(AccessPermission.class::cast)
                .min((e1, e2) -> e1.getLevel().compareTo(e2.getLevel()));

    }

    public Acl revokeAccess(
            final MemberID revoker,
            final MemberID revokedMember) {

        final Notification notification = Notification.create();

        if (this.resource.owner().equals(revokedMember))
            notification.append(ValidationError.with("The owner of the resource cannot have its access revoked"));

        if (isNull(revoker))
            notification.append(ValidationError.with("'revoker' should not be null"));

        if (isNull(revokedMember))
            notification.append(ValidationError.with("'revokedMember' should not be null"));

        if (notification.hasError())
            throw ValidationException.with("Could not revoke access", notification);

        if (revoker.equals(revokedMember))
            revokeTotal(revokedMember);

        // // TODO
        effectiveAccessPermission(revoker)
                .filter(sp -> sp.allows(AccessPermission.SHARE))
                .orElseThrow(() -> NotAllowedException.with(
                        "'granter' does not have share permission to revoke accessPermission",
                        "accessPermission level too low"));

        return this;

    }

    public Acl grantAccess(
            final MemberID granter,
            final MemberID grantee,
            final AccessPermission accessPermission) {

        if (isNull(granter))
            throw ValidationException.with(
                    "Could not grant access",
                    ValidationError.with("'granter' should not be null"));

        effectiveAccessPermission(granter)
                .filter(sp -> sp.canShare())
                .orElseThrow(() -> NotAllowedException.with(
                        "'granter' does not have share permission to grant the specified 'accessPermission'",
                        "accessPermission level too low"));

        applyEntry(grantee, accessPermission);

        this.updatedAt = Instant.now();
        this.events.add(AclUpdatedEvent.create(this));

        return this;
    }

    private <P extends Permission<?>> Acl applyEntry(
            final MemberID grantee,
            final P permission) {

        final Notification notification = Notification.create();

        if (isNull(grantee))
            notification.append(ValidationError.with("'grantee' should not be null"));
        if (isNull(permission))
            notification.append(ValidationError.with("'permission' should not be null"));

        if (notification.hasError())
            throw ValidationException.with("Could not apply entry", notification);

        final Entry<P> entry = Entry.create(grantee, permission);

        if (this.directEntries.stream().anyMatch(e -> e.isEquivalentTo(entry)))
            return this;

        this.directEntries.add(entry);

        return this;

    }

    private Acl revokeTotal(final MemberID revokedMember) {
        this.directEntries.removeIf(entry -> entry.getMember().equals(revokedMember));
        return this;
    }

    private Acl grantTotal(final MemberID grantee) {
        return this.applyEntry(grantee, AccessPermission.mostPrivileged());
    }

    public Queue<Event<?>> getEvents() {
        return new LinkedList<>(events);
    }

    public Resource<?> getResource() {
        return resource;
    }

    public Set<Entry<?>> getDirectEntries() {
        return Set.copyOf(directEntries);
    }

    public Set<Entry<?>> getInheritedEntries() {
        return Set.copyOf(inheritedEntries);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}
