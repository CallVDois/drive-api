package com.callv2.drive.domain.event;

import java.util.Optional;

@FunctionalInterface
public interface EventSource {

    Optional<Event<?>> nextEvent();

}