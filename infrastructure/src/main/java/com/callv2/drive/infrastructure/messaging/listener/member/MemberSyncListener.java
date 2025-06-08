package com.callv2.drive.infrastructure.messaging.listener.member;

import java.util.Objects;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.callv2.drive.application.member.synchronize.SynchronizeMemberInput;
import com.callv2.drive.application.member.synchronize.SynchronizeMemberUseCase;
import com.callv2.drive.infrastructure.messaging.listener.Listener;

@Component
public class MemberSyncListener implements Listener<MemberSyncEvent> {

    private final SynchronizeMemberUseCase synchronizeMemberUseCase;

    public MemberSyncListener(final SynchronizeMemberUseCase synchronizeMemberUseCase) {
        this.synchronizeMemberUseCase = Objects.requireNonNull(synchronizeMemberUseCase);
    }

    @Override
    @RabbitListener(queues = "member.sync.drive.queue")
    public void handle(final MemberSyncEvent data) {

        final MemberSyncEvent.Data eventData = data.data();

        final SynchronizeMemberInput createMemberInput = SynchronizeMemberInput.from(
                eventData.id(),
                eventData.username(),
                eventData.nickname(),
                eventData.createdAt(),
                eventData.updatedAt(),
                eventData.version());

        synchronizeMemberUseCase.execute(createMemberInput);
    }

}
