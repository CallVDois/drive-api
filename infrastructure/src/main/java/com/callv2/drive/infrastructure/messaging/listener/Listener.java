package com.callv2.drive.infrastructure.messaging.listener;

@FunctionalInterface
public interface Listener<T> {

    void handle(T data);

}
