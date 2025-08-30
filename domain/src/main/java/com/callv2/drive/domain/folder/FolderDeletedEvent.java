package com.callv2.drive.domain.folder;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

import com.callv2.drive.domain.event.Event;
import com.callv2.drive.domain.event.EventEntity;

public class FolderDeletedEvent extends Event<FolderDeletedEvent.Data> {

    private static final String ENTITY = "folder";
    private static final String ACTION = "deleted";
    private static final String VERSION = "1.0.0";

    private FolderDeletedEvent(
            Instant occurredAt,
            Set<EventEntity> relatedEntities,
            FolderDeletedEvent.Data data) {
        super(ENTITY, ACTION, VERSION, occurredAt, relatedEntities, data);
    }

    public record Data(
            String folderId,
            String ownerId,
            String folderName,
            String parentId,
            Instant createdAt) implements Serializable {

        public static Data from(final Folder folder) {
            return new Data(
                    folder.getId().getStringValue(),
                    folder.getOwner().getStringValue(),
                    folder.getName().value(),
                    folder.getParentFolder() != null ? folder.getParentFolder().getStringValue() : null,
                    folder.getCreatedAt());
        }

    }

    public static FolderDeletedEvent create(final Folder folder) {
        return new FolderDeletedEvent(Instant.now(), Set.of(EventEntity.of(folder)), Data.from(folder)); // TODO add
                                                                                                         // related
                                                                                                         // entities
    }

}