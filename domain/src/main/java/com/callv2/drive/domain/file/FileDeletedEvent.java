package com.callv2.drive.domain.file;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.callv2.drive.domain.event.Event;
import com.callv2.drive.domain.event.EventEntity;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.member.Member;

public class FileDeletedEvent extends Event<FileDeletedEvent.Data> {

    private static final String ENTITY = "file";
    private static final String ACTION = "deleted";
    private static final String VERSION = "1.0.0";

    private FileDeletedEvent() {
        super(ENTITY, ACTION, VERSION, null, null, null);
    }

    private FileDeletedEvent(
            Instant occurredAt,
            Set<EventEntity> relatedEntities,
            FileDeletedEvent.Data data) {
        super(ENTITY, ACTION, VERSION, occurredAt, relatedEntities, data);
    }

    public record Data(
            UUID fileId,
            UUID ownerId,
            UUID folderId,
            String name,
            String storageKey,
            String type,
            Long size,
            Instant creadtedAt,
            Instant updatedAt,
            Instant deletedAt) implements Serializable {

        public static Data of(final File file) {
            return new Data(
                    file.getId().getValue(),
                    file.getOwner().getValue(),
                    file.getFolder().getValue(),
                    file.getName().value(),
                    file.getContent().storageKey(),
                    file.getContent().type(),
                    file.getContent().size(),
                    file.getCreatedAt(),
                    file.getUpdatedAt(),
                    file.getDeletedAt());
        }
    }

    public static FileDeletedEvent create(final File file) {
        return new FileDeletedEvent(
                Instant.now(),
                Set.of(
                        EventEntity.of(file),
                        EventEntity.of(Member.class, file.getOwner()),
                        EventEntity.of(Folder.class, file.getFolder())),
                Data.of(file));
    }

    public static String eventKey() {
        return new FileDeletedEvent().key();
    }

}
