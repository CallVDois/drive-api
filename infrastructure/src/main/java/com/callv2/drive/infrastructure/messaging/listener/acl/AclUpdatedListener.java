package com.callv2.drive.infrastructure.messaging.listener.acl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.callv2.drive.application.acl.RecalculateAclInheritanceInput;
import com.callv2.drive.application.acl.RecalculateAclInheritanceUseCase;
import com.callv2.drive.infrastructure.acl.model.UpdateAclMessage;
import com.callv2.drive.infrastructure.messaging.listener.Listener;

@Component
public class AclUpdatedListener implements Listener<UpdateAclMessage> {

    private final RecalculateAclInheritanceUseCase recalculateAclInheritanceUseCase;

    public AclUpdatedListener(final RecalculateAclInheritanceUseCase recalculateAclInheritanceUseCase) {
        this.recalculateAclInheritanceUseCase = recalculateAclInheritanceUseCase;
    }

    @Override
    @RabbitListener(queues = "drive.acl.updated.queue")
    public void handle(final UpdateAclMessage message) {
        this.recalculateAclInheritanceUseCase.execute(new RecalculateAclInheritanceInput(message.data().aclId()));
    }

}