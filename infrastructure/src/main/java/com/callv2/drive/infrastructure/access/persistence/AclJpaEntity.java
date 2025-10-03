package com.callv2.drive.infrastructure.access.persistence;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.callv2.drive.domain.access.Acl;
import com.callv2.drive.domain.access.AclID;
import com.callv2.drive.domain.access.Entry;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.access.ResourceType;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.folder.FolderID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity(name = "Acl")
@Table(name = "acls")
public class AclJpaEntity {

    @Id
    private UUID id;

    private String resourceId;

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    @ElementCollection
    @CollectionTable(name = "acl_direct_entries", joinColumns = @JoinColumn(name = "acl_id"))
    private Set<EntryJpa> directEntries = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "acl_inherited_entries", joinColumns = @JoinColumn(name = "acl_id"))
    private Set<EntryJpa> inheritedEntries = new HashSet<>();

    private Instant createdAt;

    private Instant updatedAt;

    public AclJpaEntity() {
    }

    private AclJpaEntity(
            final UUID id,
            final String resourceId,
            final ResourceType resourceType,
            final Set<EntryJpa> directEntries,
            final Set<EntryJpa> inheritedEntries,
            final Instant createdAt,
            final Instant updatedAt) {
        this.id = id;
        this.resourceId = resourceId;
        this.resourceType = resourceType;
        this.directEntries = directEntries;
        this.inheritedEntries = inheritedEntries;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static AclJpaEntity fromDomain(final Acl acl) {

        final Set<EntryJpa> directEntries = acl.getDirectEntries().stream()
                .map(EntryJpa::fromDomain)
                .collect(java.util.stream.Collectors.toSet());

        final Set<EntryJpa> inheritedEntries = acl.getInheritedEntries().stream()
                .map(EntryJpa::fromDomain)
                .collect(java.util.stream.Collectors.toSet());

        return new AclJpaEntity(
                acl.getId().getValue(),
                acl.getResource().id().getStringValue(),
                acl.getResource().type(),
                directEntries,
                inheritedEntries,
                acl.getCreatedAt(),
                acl.getUpdatedAt());
    }

    public Acl toDomain() {

        final Set<Entry<?>> directEntries = this.directEntries.stream()
                .map(EntryJpa::toDomain)
                .collect(java.util.stream.Collectors.toSet());

        final Set<Entry<?>> inheritedEntries = this.inheritedEntries.stream()
                .map(EntryJpa::toDomain)
                .collect(java.util.stream.Collectors.toSet());

        final Resource<?> resource = switch (this.resourceType) {
            case FILE -> Resource.file(FileID.of(UUID.fromString(this.resourceId)));
            case FOLDER -> Resource.folder(FolderID.of(UUID.fromString(this.resourceId)));
            default -> throw new IllegalStateException("Unexpected value: " + this.resourceId);
        };

        return Acl.with(
                AclID.of(this.id),
                resource,
                directEntries,
                inheritedEntries,
                this.createdAt,
                this.updatedAt);
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

    public Set<EntryJpa> getDirectEntries() {
        return directEntries;
    }

    public void setDirectEntries(Set<EntryJpa> directEntries) {
        this.directEntries = directEntries;
    }

    public Set<EntryJpa> getInheritedEntries() {
        return inheritedEntries;
    }

    public void setInheritedEntries(Set<EntryJpa> inheritedEntries) {
        this.inheritedEntries = inheritedEntries;
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

}
