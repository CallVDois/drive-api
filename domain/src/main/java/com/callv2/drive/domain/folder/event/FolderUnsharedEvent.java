package com.callv2.drive.domain.folder.event;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.callv2.drive.domain.event.Event;
import com.callv2.drive.domain.event.EventEntity;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.folder.entity.FolderID;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberID;

public class FolderUnsharedEvent extends Event<FolderUnsharedEvent.Data> {

    private static final String ENTITY = "folder";
    private static final String ACTION = "unshared";
    private static final String VERSION = "1.0.0";

    private FolderUnsharedEvent() {
        super(ENTITY, ACTION, VERSION, null, null, null);
    }

    private FolderUnsharedEvent(
            Instant occurredAt,
            Set<EventEntity> relatedEntities,
            FolderUnsharedEvent.Data data) {
        super(ENTITY, ACTION, VERSION, occurredAt, relatedEntities, data);
    }

    public record Data(
            UUID folderId,
            UUID ownerId,
            UUID unsharedBy,
            UUID unsharedTo,
            Instant unsharedAt) implements Serializable {

        public static Data from(
                final Folder folder,
                final MemberID unsharedBy,
                final MemberID unsharedTo,
                final Instant unsharedAt) {
            return new Data(
                    folder.getId().getValue(),
                    folder.getOwner().getValue(),
                    unsharedBy.getValue(),
                    unsharedTo.getValue(),
                    unsharedAt);
        }

    }

    public static FolderUnsharedEvent create(
            final Folder folder,
            final MemberID unsharedBy,
            final MemberID unsharedTo,
            final Instant unsharedAt) {
        return new FolderUnsharedEvent(
                Instant.now(),
                Set.of(EventEntity.of(folder), EventEntity.of(Member.class, folder.getOwner())),
                Data.from(folder, unsharedBy, unsharedTo, unsharedAt));
    }

    public static String eventKey() {
        return new FolderUnsharedEvent().key();
    }
}
