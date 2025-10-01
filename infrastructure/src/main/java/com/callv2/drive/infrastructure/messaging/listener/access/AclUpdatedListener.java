package com.callv2.drive.infrastructure.messaging.listener.access;

import com.callv2.drive.infrastructure.access.model.UpdateAclMessage;
import com.callv2.drive.infrastructure.messaging.listener.Listener;

public class AclUpdatedListener implements Listener<UpdateAclMessage> {

    @Override
    public void handle(final UpdateAclMessage message) {

    }

}