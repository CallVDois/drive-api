package com.callv2.drive.domain.folder.event;

import static java.util.Objects.nonNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.callv2.drive.domain.event.Event;
import com.callv2.drive.domain.event.EventEntity;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.member.Member;

public class FolderCreatedEvent extends Event<FolderCreatedEvent.Data> {

    private static final String ENTITY = "folder";
    private static final String ACTION = "created";
    private static final String VERSION = "1.0.0";

    public FolderCreatedEvent(
            final Instant occurredAt,
            final Set<EventEntity> relatedEntities,
            final Data data) {
        super(ENTITY, ACTION, VERSION, occurredAt, relatedEntities, data);
    }

    private FolderCreatedEvent() {
        super(ENTITY, ACTION, VERSION, null, null, null);
    }

    public record Data(
            UUID folderId,
            UUID creatorId,
            UUID ownerId,
            UUID parentFolderId,
            String folderName,
            Boolean isRootFolder,
            Boolean isDefaultSharedInbox,
            Instant createdAt) implements Serializable {

        public static Data from(final Folder folder) {
            return new Data(
                    folder.getId().getValue(),
                    folder.getCreator().getValue(),
                    folder.getOwner().getValue(),
                    folder.getParentFolder() != null ? folder.getParentFolder().getValue() : null,
                    folder.getName().value(),
                    folder.isRootFolder(),
                    folder.isDefaultSharedInbox(),
                    folder.getCreatedAt());
        }

    }

    public static FolderCreatedEvent create(final Folder folder) {

        final EventEntity parentFolderEventEntity = nonNull(folder.getParentFolder())
                ? EventEntity.of(Folder.class, folder.getParentFolder())
                : null;

        return new FolderCreatedEvent(
                Instant.now(),
                Stream.of(
                        EventEntity.of(folder),
                        parentFolderEventEntity,
                        EventEntity.of(Member.class, folder.getOwner()),
                        EventEntity.of(Member.class, folder.getCreator()))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()),
                Data.from(folder));
    }

    public static String eventKey() {
        return new FolderCreatedEvent().key();
    }

}
