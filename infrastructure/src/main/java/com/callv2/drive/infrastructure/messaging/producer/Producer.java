package com.callv2.drive.infrastructure.messaging.producer;

@FunctionalInterface
public interface Producer<T> {

    void send(T data);

}