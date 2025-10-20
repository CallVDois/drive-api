package com.callv2.drive.infrastructure.file.event;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.callv2.drive.domain.event.Event;
import com.callv2.drive.domain.event.EventHandler;
import com.callv2.drive.domain.file.FileSharedEvent;
import com.callv2.drive.infrastructure.messaging.producer.Producer;

@Component
public class FileSharedEventMessagingHandler implements EventHandler<FileSharedEvent.Data> {

    private static final String EVENT_KEY = FileSharedEvent.eventKey();
    private final Producer<Event<FileSharedEvent.Data>> producer;

    public FileSharedEventMessagingHandler(final Producer<Event<FileSharedEvent.Data>> producer) {
        this.producer = Objects.requireNonNull(producer);
    }

    @Override
    public String eventKey() {
        return EVENT_KEY;
    }

    @Override
    public void handle(final Event<FileSharedEvent.Data> event) {
        producer.send(event);
    }

}
