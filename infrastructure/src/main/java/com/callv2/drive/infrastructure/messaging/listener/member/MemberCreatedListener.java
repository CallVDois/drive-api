package com.callv2.drive.infrastructure.messaging.listener.member;

import java.util.Objects;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.callv2.drive.application.member.create.CreateMemberInput;
import com.callv2.drive.application.member.create.CreateMemberUseCase;
import com.callv2.drive.infrastructure.messaging.listener.Listener;

@Component
public class MemberCreatedListener implements Listener<MemberCreatedEvent> {

    private final CreateMemberUseCase createMemberUseCase;

    public MemberCreatedListener(final CreateMemberUseCase createMemberUseCase) {
        this.createMemberUseCase = Objects.requireNonNull(createMemberUseCase);
    }

    @Override
    @RabbitListener(queues = "drive.member.created")
    public void handle(final MemberCreatedEvent data) {

        final MemberCreatedEvent.Data eventData = data.data();

        final CreateMemberInput createMemberInput = CreateMemberInput.from(
                eventData.id(),
                eventData.username(),
                eventData.nickname());

        createMemberUseCase.execute(createMemberInput);
    }

}
