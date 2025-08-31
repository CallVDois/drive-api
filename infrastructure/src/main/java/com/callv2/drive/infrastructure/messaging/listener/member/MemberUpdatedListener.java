package com.callv2.drive.infrastructure.messaging.listener.member;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import com.callv2.drive.application.member.synchronize.SynchronizeMemberInput;
import com.callv2.drive.application.member.synchronize.SynchronizeMemberUseCase;
import com.callv2.drive.domain.exception.AlreadyExistsException;
import com.callv2.drive.domain.exception.IdMismatchException;
import com.callv2.drive.domain.exception.SynchronizedVersionOutdatedException;
import com.callv2.drive.infrastructure.member.model.MemberUpdatedMessage;
import com.callv2.drive.infrastructure.messaging.listener.Listener;

@Component
public class MemberUpdatedListener implements Listener<MemberUpdatedMessage> {

    private static final Logger log = LogManager.getLogger(MemberUpdatedListener.class);

    private final SynchronizeMemberUseCase synchronizeMemberUseCase;

    public MemberUpdatedListener(final SynchronizeMemberUseCase synchronizeMemberUseCase) {
        this.synchronizeMemberUseCase = Objects.requireNonNull(synchronizeMemberUseCase);
    }

    @Override
    @RabbitListener(queues = "drive.member.updated.queue")
    public void handle(final MemberUpdatedMessage data) {

        final MemberUpdatedMessage.Data eventData = data.data();

        final Boolean hasSystemAccess = eventData.systems() != null && eventData.systems().contains("DRIVE");

        final SynchronizeMemberInput createMemberInput = SynchronizeMemberInput.from(
                eventData.id(),
                eventData.username(),
                eventData.nickname(),
                hasSystemAccess,
                eventData.createdAt(),
                eventData.updatedAt(),
                eventData.synchronizedVersion());

        try {
            synchronizeMemberUseCase.execute(createMemberInput);
        } catch (OptimisticLockingFailureException
                | SynchronizedVersionOutdatedException
                | IdMismatchException
                | AlreadyExistsException e) {
            log.warn("Error synchronizing member: {}", e.getMessage(), e);
        }

    }

}
