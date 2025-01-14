package com.callv2.drive.infrastructure.folder.persistence;

import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.folder.FolderName;
import com.callv2.drive.domain.folder.SubFolder;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity(name = "SubFolder")
@Table(name = "sub_folders")
public class SubFolderJpaEntity {

    @EmbeddedId
    private SubFolderID id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("parentFolderId")
    private FolderJpaEntity parentFolder;

    public SubFolderJpaEntity() {
    }

    private SubFolderJpaEntity(
            final SubFolderID id,
            final String name,
            final FolderJpaEntity parentFolder) {
        this.id = id;
        this.name = name;
        this.parentFolder = parentFolder;
    }

    public static SubFolderJpaEntity with(final FolderJpaEntity folder, final SubFolder subFolder) {
        return new SubFolderJpaEntity(
                SubFolderID.with(folder.getId(), subFolder.id().getValue()),
                subFolder.name().value(),
                folder);
    }

    public SubFolder toDomain() {
        return SubFolder.with(FolderID.of(getId().getParentFolderId()), FolderName.of(getName()));
    }

    public SubFolderID getId() {
        return id;
    }

    public void setId(SubFolderID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FolderJpaEntity getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(FolderJpaEntity parentFolder) {
        this.parentFolder = parentFolder;
    }

}
