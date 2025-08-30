package com.callv2.drive.domain.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class EventDispatcher {

    private final ConcurrentHashMap<String, List<EventHandler<?>>> handlers = new ConcurrentHashMap<>();

    public void register(final String eventKey, final EventHandler<?> handler) {
        this.handlers.computeIfAbsent(eventKey, k -> new ArrayList<>()).add(handler);
    }

    public void unregister(final Event<?> event, final EventHandler<?> handler) {
        this.handlers.computeIfPresent(event.key(), (k, v) -> {
            v.remove(handler);
            return v.isEmpty() ? null : v;
        });
    }

    public void unregisterAll(final String eventKey) {
        this.handlers.remove(eventKey);
    }

    public <D extends Serializable> void notify(final Event<D> event) {
        @SuppressWarnings("unchecked")
        final List<EventHandler<D>> handlers = (List<EventHandler<D>>) Optional
                .ofNullable(this.handlers.get(event.key()))
                .filter(h -> !h.isEmpty())
                .map(h -> (List<EventHandler<D>>) (List<?>) h)
                .orElse(List.of());

        handlers.forEach(handler -> handler.handle(event));
    }

    public void notify(final EventSource source) {
        var event = source.nextEvent();
        while (event.isPresent()) {
            notify(event.get());
            event = source.nextEvent();
        }
    }

}