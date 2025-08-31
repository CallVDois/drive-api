package com.callv2.drive.infrastructure.configuration.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.drive.domain.event.Event;
import com.callv2.drive.domain.file.FileDeletedEvent;
import com.callv2.drive.infrastructure.messaging.producer.rabbitmq.RabbitMQProducer;

@Configuration
public class RabbitMQConfig {

    private static final String DRIVE_EXCHANGE_NAME = "drive.exchange";
    private static final String MEMBER_EXCHANGE_NAME = "member.exchange";
    private static final String MEMBER_EXCHANGE_DLX_NAME = "member.dlx.exchange";

    private static final String FILE_DELETED_ROUTING_KEY = "file.deleted";

    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitMQProducer<Event<FileDeletedEvent.Data>> memberCreatedProducer(final RabbitTemplate rabbitTemplate) {
        return new RabbitMQProducer<>(
                DRIVE_EXCHANGE_NAME,
                FILE_DELETED_ROUTING_KEY,
                rabbitTemplate);
    }

    @Configuration
    static class Admin {

        private final TopicExchange driveExchange = new TopicExchange(DRIVE_EXCHANGE_NAME);
        private final TopicExchange memberExchange = new TopicExchange(MEMBER_EXCHANGE_NAME);
        private final TopicExchange memberDlxExchange = new TopicExchange(MEMBER_EXCHANGE_DLX_NAME);

        private final Queue fileDeletedQueue = QueueBuilder
                .durable("file.deleted.queue")
                .build();

        private final Queue memberSyncQueue = QueueBuilder
                .durable("member.sync.drive.queue")
                .deadLetterExchange("member.dlx.exchange")
                .deadLetterRoutingKey("member.sync.deadletter")
                .build();

        private final Queue memberSyncDeadLetterQueue = QueueBuilder
                .durable("member.sync.drive.queue.deadletter")
                .build();

        private final Binding fileDeletedBinding = BindingBuilder
                .bind(fileDeletedQueue)
                .to(driveExchange)
                .with(FILE_DELETED_ROUTING_KEY);

        private final Binding memberUpdatedSyncBindings = BindingBuilder
                .bind(memberSyncQueue)
                .to(memberExchange)
                .with("member.updated");

        private final Binding memberSyncDeadLetterBinding = BindingBuilder
                .bind(memberSyncDeadLetterQueue)
                .to(memberDlxExchange)
                .with("member.sync.deadletter");

        private final Binding memberCreatedSyncBindings = BindingBuilder
                .bind(memberSyncQueue)
                .to(memberExchange)
                .with("member.created");

        @Bean
        TopicExchange driveExchange() {
            return driveExchange;
        }

        @Bean
        TopicExchange membExchange() {
            return memberExchange;
        }

        @Bean
        TopicExchange memberDlxExchange() {
            return memberDlxExchange;
        }

        @Bean
        Queue fileDeletedQueue() {
            return fileDeletedQueue;
        }

        @Bean
        Queue memberSyncQueue() {
            return memberSyncQueue;
        }

        @Bean
        Queue memberSyncDeadLetterQueue() {
            return memberSyncDeadLetterQueue;
        }

        @Bean
        Binding fileDeletedBinding() {
            return fileDeletedBinding;
        }

        @Bean
        Binding memberCreatedSyncBindings() {
            return memberCreatedSyncBindings;
        }

        @Bean
        Binding memberUpdatedSyncBindings() {
            return memberUpdatedSyncBindings;
        }

        @Bean
        Binding memberSyncDeadLetterBinding() {
            return memberSyncDeadLetterBinding;
        }
    }

}
