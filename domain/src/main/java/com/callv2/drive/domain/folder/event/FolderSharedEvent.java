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

public class FolderSharedEvent extends Event<FolderSharedEvent.Data> {

    private static final String ENTITY = "folder";
    private static final String ACTION = "shared";
    private static final String VERSION = "1.0.0";

    private FolderSharedEvent() {
        super(ENTITY, ACTION, VERSION, null, null, null);
    }

    private FolderSharedEvent(
            Instant occurredAt,
            Set<EventEntity> relatedEntities,
            FolderSharedEvent.Data data) {
        super(ENTITY, ACTION, VERSION, occurredAt, relatedEntities, data);
    }

    public record Data(
            UUID folderId,
            UUID ownerId,
            UUID sharedById,
            UUID sharedToId,
            UUID virtualFolderId,
            Instant sharedAt) implements Serializable {

        public static Data from(
                final Folder folder,
                final MemberID sharedBy,
                final MemberID sharedTo,
                final FolderID virtualFolder,
                final Instant sharedAt) {
            return new Data(
                    folder.getId().getValue(),
                    folder.getOwner().getValue(),
                    sharedBy.getValue(),
                    sharedTo.getValue(),
                    virtualFolder.getValue(),
                    sharedAt);
        }

    }

    public static FolderSharedEvent create(
            final Folder folder,
            final MemberID sharedBy,
            final MemberID sharedTo,
            final FolderID virtualFolder,
            final Instant sharedAt) {
        return new FolderSharedEvent(Instant.now(),
                Set.of(EventEntity.of(folder), EventEntity.of(Member.class, folder.getOwner())),
                Data.from(folder, sharedBy, sharedTo, virtualFolder, sharedAt));
    }

    public static String eventKey() {
        return new FolderSharedEvent().key();
    }

}
