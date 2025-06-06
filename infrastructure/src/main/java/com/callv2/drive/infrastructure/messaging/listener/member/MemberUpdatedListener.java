package com.callv2.drive.infrastructure.messaging.listener.member;

import java.util.Objects;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.callv2.drive.application.member.synchronize.SynchronizeMemberInput;
import com.callv2.drive.application.member.synchronize.SynchronizeMemberUseCase;
import com.callv2.drive.infrastructure.messaging.listener.Listener;

@Component
public class MemberUpdatedListener implements Listener<MemberUpdatedEvent> {

    private final SynchronizeMemberUseCase synchronizeMemberUseCase;

    public MemberUpdatedListener(final SynchronizeMemberUseCase synchronizeMemberUseCase) {
        this.synchronizeMemberUseCase = Objects.requireNonNull(synchronizeMemberUseCase);
    }

    @Override
    @RabbitListener(queues = "drive.member.created")
    public void handle(final MemberUpdatedEvent data) {

        final MemberUpdatedEvent.Data eventData = data.data();

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
