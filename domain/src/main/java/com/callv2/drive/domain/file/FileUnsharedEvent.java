package com.callv2.drive.domain.file;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.callv2.drive.domain.event.Event;
import com.callv2.drive.domain.event.EventEntity;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberID;

public class FileUnsharedEvent extends Event<FileUnsharedEvent.Data> {

    private static final String ENTITY = "file";
    private static final String ACTION = "unshared";
    private static final String VERSION = "1.0.0";

    private FileUnsharedEvent() {
        super(ENTITY, ACTION, VERSION, null, null, null);
    }

    private FileUnsharedEvent(
            final Instant occurredAt,
            final Set<EventEntity> relatedEntities,
            final FileUnsharedEvent.Data data) {
        super(ENTITY, ACTION, VERSION, occurredAt, relatedEntities, data);
    }

    public record Data(
            UUID fileId,
            UUID fileOwnerId,
            UUID unsharedToId,
            UUID unsharedByID,
            Instant unsharedAt) implements Serializable {

        public static Data of(
                final File file,
                final MemberID unsharedToId,
                final MemberID unsharedById,
                final Instant unsharedAt) {
            return new Data(
                    file.getId().getValue(),
                    file.getOwner().getValue(),
                    unsharedToId.getValue(),
                    unsharedById.getValue(),
                    unsharedAt);
        }
    }

    public static FileUnsharedEvent create(
            final File file,
            final MemberID unsharedToId,
            final MemberID unsharedById) {
        return new FileUnsharedEvent(
                Instant.now(),
                Stream.of(
                        EventEntity.of(file),
                        EventEntity.of(Member.class, file.getOwner()),
                        EventEntity.of(Member.class, unsharedToId),
                        EventEntity.of(Member.class, unsharedById),
                        EventEntity.of(Folder.class, file.getFolder()))
                        .collect(Collectors.toSet()),
                Data.of(file, unsharedToId, unsharedById, Instant.now()));
    }

    public static String eventKey() {
        return new FileUnsharedEvent().key();
    }

}
