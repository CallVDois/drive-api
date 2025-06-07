package com.callv2.drive.infrastructure.configuration.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.drive.infrastructure.configuration.properties.messaging.RabbitMQQueueProperties;

@Configuration
public class RabbitMQConfig {

    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    @ConfigurationProperties("rabbitmq.queue.member-sync")
    RabbitMQQueueProperties memberSyncProperties() {
        return new RabbitMQQueueProperties();
    }

    @Configuration
    static class Admin {

        @Bean
        Exchange memberSyncExchange(@Qualifier("memberSyncProperties") final RabbitMQQueueProperties props) {
            return new TopicExchange(props.getExchange());
        }

        @Bean
        Queue memberSyncQueue(@Qualifier("memberSyncProperties") final RabbitMQQueueProperties props) {
            return new Queue(props.getQueue());
        }

        @Bean
        Binding videoCreatedBinding(
                @Qualifier("memberSyncExchange") final DirectExchange exchange,
                @Qualifier("memberSyncQueue") final Queue queue,
                @Qualifier("memberSyncProperties") final RabbitMQQueueProperties props) {
            return BindingBuilder.bind(queue).to(exchange).with(props.getRoutingKey());
        }

    }

}
