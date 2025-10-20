package com.callv2.drive.infrastructure.folder.event;

import org.springframework.stereotype.Component;

import com.callv2.drive.domain.event.Event;
import com.callv2.drive.domain.event.EventHandler;
import com.callv2.drive.domain.folder.event.FolderCreatedEvent;
import com.callv2.drive.infrastructure.messaging.producer.Producer;

@Component
public class FolderCreatedEventMessagingHandler implements EventHandler<FolderCreatedEvent.Data> {

    private static final String EVENT_KEY = FolderCreatedEvent.eventKey();
    private final Producer<Event<FolderCreatedEvent.Data>> producer;

    public FolderCreatedEventMessagingHandler(final Producer<Event<FolderCreatedEvent.Data>> producer) {
        this.producer = producer;
    }

    @Override
    public String eventKey() {
        return EVENT_KEY;
    }

    @Override
    public void handle(final Event<FolderCreatedEvent.Data> event) {
        this.producer.send(event);
    }

}
