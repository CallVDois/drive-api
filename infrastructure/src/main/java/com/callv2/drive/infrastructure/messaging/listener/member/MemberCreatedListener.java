package com.callv2.drive.infrastructure.messaging.listener.member;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.callv2.drive.application.member.create.CreateMemberInput;
import com.callv2.drive.application.member.create.CreateMemberUseCase;
import com.callv2.drive.infrastructure.messaging.listener.Listener;

@Component
public class MemberCreatedListener implements Listener<MemberCreatedListener.Event> {

    private final CreateMemberUseCase createMemberUseCase;

    public MemberCreatedListener(final CreateMemberUseCase createMemberUseCase) {
        this.createMemberUseCase = Objects.requireNonNull(createMemberUseCase);
    }

    @Override
    @RabbitListener(queues = "drive.member.created")
    public void handle(final MemberCreatedListener.Event data) {

        final Event.Data eventData = data.data();

        final CreateMemberInput createMemberInput = CreateMemberInput.from(
                eventData.id,
                eventData.username,
                eventData.nickname);

        createMemberUseCase.execute(createMemberInput);
    }

    public record Event(
            String id,
            String source,
            Event.Data data,
            Instant occurredAt) {

        public record Data(
                String id,
                String username,
                String email,
                String nickname,
                boolean isActive,
                Instant createdAt,
                Instant updatedAt) implements Serializable {

        }

    }

}
