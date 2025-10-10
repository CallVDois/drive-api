package com.callv2.drive.infrastructure.folder.event;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.callv2.drive.domain.event.Event;
import com.callv2.drive.domain.event.EventHandler;
import com.callv2.drive.domain.folder.FolderSharedEvent;
import com.callv2.drive.infrastructure.messaging.producer.Producer;

@Component
public class FolderSharedEventMessagingHandler implements EventHandler<FolderSharedEvent.Data> {

    private static final String EVENT_KEY = FolderSharedEvent.eventKey();
    private final Producer<Event<FolderSharedEvent.Data>> producer;

    public FolderSharedEventMessagingHandler(final Producer<Event<FolderSharedEvent.Data>> producer) {
        this.producer = Objects.requireNonNull(producer);
    }

    @Override
    public String eventKey() {
        return EVENT_KEY;
    }

    @Override
    public void handle(final Event<FolderSharedEvent.Data> event) {
        producer.send(event);
    }

}
