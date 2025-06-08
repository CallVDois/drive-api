package com.callv2.drive.infrastructure.configuration.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Configuration
    static class Admin {

        @Bean
        TopicExchange memberExchange() {
            return new TopicExchange("member.exchange");
        }

        @Bean
        Queue memberSyncQueue() {
            return new Queue("member.sync.drive.queue");
        }

        @Bean
        Binding memberCreatedSyncBindings(
                @Qualifier("memberExchange") final TopicExchange exchange,
                @Qualifier("memberSyncQueue") final Queue queue) {
            return BindingBuilder.bind(queue).to(exchange).with("member.created");
        }

        @Bean
        Binding memberUpdatedSyncBindings(
                @Qualifier("memberExchange") final TopicExchange exchange,
                @Qualifier("memberSyncQueue") final Queue queue) {
            return BindingBuilder.bind(queue).to(exchange).with("member.updated");
        }

    }

}
