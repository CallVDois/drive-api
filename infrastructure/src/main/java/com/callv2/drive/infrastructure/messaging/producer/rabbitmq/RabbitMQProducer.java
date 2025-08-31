package com.callv2.drive.infrastructure.messaging.producer.rabbitmq;

import java.io.Serializable;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.callv2.drive.infrastructure.messaging.producer.Producer;

public class RabbitMQProducer<T extends Serializable> implements Producer<T> {

    private final String exchange;
    private final String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(
            final String exchange,
            final String routingKey,
            final RabbitTemplate rabbitTemplate) {

        this.exchange = exchange;
        this.routingKey = routingKey;

        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void send(T data) {
        this.rabbitTemplate.convertAndSend(this.exchange, this.routingKey, data);
    }

}