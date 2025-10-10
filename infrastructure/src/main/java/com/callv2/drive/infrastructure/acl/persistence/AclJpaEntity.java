package com.callv2.drive.infrastructure.acl.persistence;

import java.time.Instant;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import com.callv2.drive.domain.Identifier;
import com.callv2.drive.domain.acl.Acl;
import com.callv2.drive.domain.acl.AclID;
import com.callv2.drive.domain.acl.Entry;
import com.callv2.drive.domain.acl.Resource;
import com.callv2.drive.domain.acl.ResourceType;
import com.callv2.drive.domain.event.Event;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.folder.entity.FolderID;
import com.callv2.drive.domain.member.MemberID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity(name = "Acl")
@Table(name = "acls")
public class AclJpaEntity {

    @Id
    private UUID id;

    @Column(name = "resource_id", nullable = false)
    private String resourceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false)
    private ResourceType resourceType;

    @Column(name = "resource_owner", nullable = false)
    private UUID resourceOwner;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Transient
    private Queue<Event<?>> events;

    public AclJpaEntity() {
    }

    private AclJpaEntity(
            final UUID id,
            final String resourceId,
            final ResourceType resourceType,
            final UUID resourceOwner,
            final Instant createdAt,
            final Instant updatedAt,
            final Queue<Event<?>> events) {
        this.id = id;
        this.resourceId = resourceId;
        this.resourceType = resourceType;
        this.resourceOwner = resourceOwner;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.events = events;
    }

    public static AclJpaEntity fromDomain(final Acl acl) {

        return new AclJpaEntity(
                acl.getId().getValue(),
                acl.getResource().id().getStringValue(),
                acl.getResource().type(),
                acl.getResource().owner().getValue(),
                acl.getCreatedAt(),
                acl.getUpdatedAt(),
                acl.getEvents());

    }

    public Acl toDomain(final Set<Entry<?>> directEntries, final Set<Entry<?>> inheritedEntries) {

        final Resource<?> resource = switch (this.resourceType) {
            case FILE -> toResource(FileID.of(UUID.fromString(this.resourceId)));
            case FOLDER -> toResource(FolderID.of(UUID.fromString(this.resourceId)));
            default -> throw new IllegalStateException("Unexpected value: " + this.resourceId);
        };

        return Acl.with(
                AclID.of(this.id),
                resource,
                directEntries,
                inheritedEntries,
                this.createdAt,
                this.updatedAt,
                this.events);
    }

    private <I extends Identifier<?>> Resource<I> toResource(I identifier) {
        return new Resource<>(identifier, this.resourceType, MemberID.of(this.resourceOwner));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public UUID getResourceOwner() {
        return resourceOwner;
    }

    public void setResourceOwner(UUID resourceOwner) {
        this.resourceOwner = resourceOwner;
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

    public Queue<Event<?>> getEvents() {
        return events;
    }

    public void setEvents(Queue<Event<?>> events) {
        this.events = events;
    }

}
