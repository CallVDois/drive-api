package com.callv2.drive.infrastructure.messaging.listener.member;

import java.util.Objects;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.callv2.drive.application.member.create.CreateMemberInput;
import com.callv2.drive.application.member.create.CreateMemberUseCase;
import com.callv2.drive.application.member.update.UpdateMemberInput;
import com.callv2.drive.application.member.update.UpdateMemberUseCase;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.infrastructure.messaging.listener.Listener;

@Component
public class MemberUpdatedListener implements Listener<MemberUpdatedEvent> {

    private final UpdateMemberUseCase updateMemberUseCase;
    private final CreateMemberUseCase createMemberUseCase;

    public MemberUpdatedListener(
            final UpdateMemberUseCase updateMemberUseCase,
            final CreateMemberUseCase createMemberUseCase) {
        this.updateMemberUseCase = Objects.requireNonNull(updateMemberUseCase);
        this.createMemberUseCase = Objects.requireNonNull(createMemberUseCase);
    }

    @Override
    @RabbitListener(queues = "drive.member.updated")
    public void handle(final MemberUpdatedEvent data) {

        final MemberUpdatedEvent.Data eventData = data.data();

        try {
            updateMember(eventData);
        } catch (final NotFoundException e) {
            createMember(eventData);
        }
    }

    private void updateMember(final MemberUpdatedEvent.Data eventData) {
        final UpdateMemberInput updateMemberInput = UpdateMemberInput.from(
                eventData.id(),
                eventData.username(),
                eventData.nickname());

        this.updateMemberUseCase.execute(updateMemberInput);
    }

    private void createMember(final MemberUpdatedEvent.Data eventData) {
        final CreateMemberInput updateMemberInput = CreateMemberInput.from(
                eventData.id(),
                eventData.username(),
                eventData.nickname());

        this.createMemberUseCase.execute(updateMemberInput);
    }

}
