package com.callv2.drive.infrastructure.acl.event;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.callv2.drive.domain.acl.AclUpdatedEvent;
import com.callv2.drive.domain.event.Event;
import com.callv2.drive.domain.event.EventHandler;
import com.callv2.drive.infrastructure.messaging.producer.Producer;

@Component
public class AclUpdatedEventMessagingHandler implements EventHandler<AclUpdatedEvent.Data> {

    private static final String EVENT_KEY = AclUpdatedEvent.eventKey();
    private final Producer<Event<AclUpdatedEvent.Data>> producer;

    public AclUpdatedEventMessagingHandler(final Producer<Event<AclUpdatedEvent.Data>> producer) {
        this.producer = Objects.requireNonNull(producer);
    }

    @Override
    public String eventKey() {
        return EVENT_KEY;
    }

    @Override
    public void handle(final Event<AclUpdatedEvent.Data> event) {
        producer.send(event);
    }

}
