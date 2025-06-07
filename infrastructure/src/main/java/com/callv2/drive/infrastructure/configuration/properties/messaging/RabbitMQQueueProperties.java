package com.callv2.drive.infrastructure.configuration.properties.messaging;

public class RabbitMQQueueProperties {

    private String exchange;

    private String routingKey;

    private String queue;

    public RabbitMQQueueProperties() {
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

}
