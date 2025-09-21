package com.callv2.drive.infrastructure.file.event;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.callv2.drive.domain.event.Event;
import com.callv2.drive.domain.event.EventHandler;
import com.callv2.drive.domain.file.FileDeletedEvent;
import com.callv2.drive.infrastructure.messaging.producer.Producer;

@Component
public class FileDeletedEventMessagingHandler implements EventHandler<FileDeletedEvent.Data> {

    private static final String EVENT_KEY = FileDeletedEvent.eventKey();
    private final Producer<Event<FileDeletedEvent.Data>> producer;

    public FileDeletedEventMessagingHandler(final Producer<Event<FileDeletedEvent.Data>> producer) {
        this.producer = Objects.requireNonNull(producer);
    }

    @Override
    public String eventKey() {
        return EVENT_KEY;
    }

    @Override
    public void handle(final Event<FileDeletedEvent.Data> event) {
        producer.send(event);
    }

}