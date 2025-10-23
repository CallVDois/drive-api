package com.callv2.drive.domain.file;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.callv2.drive.domain.event.Event;
import com.callv2.drive.domain.event.EventEntity;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.member.Member;

public class FileSharedEvent extends Event<FileSharedEvent.Data> {

    private static final String ENTITY = "file";
    private static final String ACTION = "shared";
    private static final String VERSION = "1.0.0";

    private FileSharedEvent() {
        super(ENTITY, ACTION, VERSION, null, null, null);
    }

    private FileSharedEvent(
            final Instant occurredAt,
            final Set<EventEntity> relatedEntities,
            final FileSharedEvent.Data data) {
        super(ENTITY, ACTION, VERSION, occurredAt, relatedEntities, data);
    }

    public record Data(
            UUID fileId,
            UUID fileOwnerId,
            UUID sharingId,
            UUID sharedToId,
            UUID sharedByID,
            UUID virtualFolderId,
            Instant sharedAt) implements Serializable {

        public static Data of(final File file, final FileSharing sharing) {
            return new Data(
                    file.getId().getValue(),
                    file.getOwner().getValue(),
                    sharing.getId().getValue(),
                    sharing.getSharedTo().getValue(),
                    sharing.getSharedBy().getValue(),
                    sharing.getVirtualFolder().getValue(),
                    sharing.getCreatedAt());
        }
    }

    public static FileSharedEvent create(final File file, final FileSharing sharing) {
        return new FileSharedEvent(
                Instant.now(),
                Stream.of(
                        EventEntity.of(file),
                        EventEntity.of(Member.class, file.getOwner()),
                        EventEntity.of(Member.class, sharing.getSharedTo()),
                        EventEntity.of(Member.class, sharing.getSharedBy()),
                        EventEntity.of(Folder.class, file.getFolder()),
                        EventEntity.of(Folder.class, sharing.getVirtualFolder()))
                        .collect(Collectors.toSet()),
                Data.of(file, sharing));
    }

    public static String eventKey() {
        return new FileSharedEvent().key();
    }

}
