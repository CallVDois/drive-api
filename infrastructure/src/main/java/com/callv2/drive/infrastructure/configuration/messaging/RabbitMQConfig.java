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
import com.callv2.drive.domain.file.FileSharedEvent;
import com.callv2.drive.infrastructure.messaging.producer.rabbitmq.RabbitMQProducer;

@Configuration
public class RabbitMQConfig {

    private static final String EVENT_HUB_EXCHANGE_NAME = "eventhub.exchange";
    private static final String EVENT_HUB_EXCHANGE_ROUTING_KEY = "drive.#.event";
    private static final String EVENT_HUB_MEMBER_CREATED_ROUTING_KEY = "member.member.created.event";
    private static final String EVENT_HUB_MEMBER_UPDATED_ROUTING_KEY = "member.member.updated.event";

    private static final String DRIVE_EXCHANGE_NAME = "drive.exchange";
    private static final String DRIVE_DLX_EXCHANGE_NAME = "drive.dlx.exchange";

    private static final String FILE_DELETED_QUEUE_NAME = "drive.file.deleted.queue";
    private static final String FILE_DELETED_ROUTING_KEY = "drive.file.deleted.event";
    private static final String FILE_DELETED_DLX_ROUTING_KEY = "drive.file.deleted.event.deadletter";
    private static final String FILE_DELETED_DLQ_QUEUE = "drive.file.deleted.queue.dlq";

    private static final String FILE_SHARED_QUEUE_NAME = "drive.file.shared.queue";
    private static final String FILE_SHARED_ROUTING_KEY = "drive.file.shared.event";
    private static final String FILE_SHARED_DLX_ROUTING_KEY = "drive.file.shared.event.deadletter";
    private static final String FILE_SHARED_DLQ_QUEUE = "drive.file.shared.queue.dlq";

    private static final String MEMBER_CREATED_QUEUE_NAME = "drive.member.created.queue";
    private static final String MEMBER_CREATED_ROUTING_KEY = EVENT_HUB_MEMBER_CREATED_ROUTING_KEY;
    private static final String MEMBER_CREATED_DLX_ROUTING_KEY = "drive.member.created.event.deadletter";
    private static final String MEMBER_CREATED_DLQ_QUEUE = "drive.member.created.queue.dlq";

    private static final String MEMBER_UPDATED_QUEUE_NAME = "drive.member.updated.queue";
    private static final String MEMBER_UPDATED_ROUTING_KEY = EVENT_HUB_MEMBER_UPDATED_ROUTING_KEY;
    private static final String MEMBER_UPDATED_DLX_ROUTING_KEY = "drive.member.updated.event.deadletter";
    private static final String MEMBER_UPDATED_DLQ_QUEUE = "drive.member.updated.queue.dlq";

    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitMQProducer<Event<FileDeletedEvent.Data>> fileDeletedProducer(final RabbitTemplate rabbitTemplate) {
        return new RabbitMQProducer<>(
                DRIVE_EXCHANGE_NAME,
                FILE_DELETED_ROUTING_KEY,
                rabbitTemplate);
    }

    @Bean
    RabbitMQProducer<Event<FileSharedEvent.Data>> fileSharedProducer(final RabbitTemplate rabbitTemplate) {
        return new RabbitMQProducer<>(
                DRIVE_EXCHANGE_NAME,
                FILE_SHARED_ROUTING_KEY,
                rabbitTemplate);
    }

    @Configuration
    static class Admin {

        private final TopicExchange eventHubExchange = new TopicExchange(EVENT_HUB_EXCHANGE_NAME);

        private final TopicExchange driveExchange = new TopicExchange(DRIVE_EXCHANGE_NAME);
        private final TopicExchange driveDlxExchange = new TopicExchange(DRIVE_DLX_EXCHANGE_NAME);

        public final Binding driveEventsBinding = BindingBuilder
                .bind(eventHubExchange)
                .to(driveExchange)
                .with(EVENT_HUB_EXCHANGE_ROUTING_KEY);

        private final Binding eventHubMemberCreatedEventBinding = BindingBuilder
                .bind(driveExchange)
                .to(eventHubExchange)
                .with(EVENT_HUB_MEMBER_CREATED_ROUTING_KEY);

        private final Binding eventHubMemberUpdatedEventBinding = BindingBuilder
                .bind(driveExchange)
                .to(eventHubExchange)
                .with(EVENT_HUB_MEMBER_UPDATED_ROUTING_KEY);

        private final Queue fileDeletedQueue = QueueBuilder
                .durable(FILE_DELETED_QUEUE_NAME)
                .deadLetterExchange(DRIVE_DLX_EXCHANGE_NAME)
                .deadLetterRoutingKey(FILE_DELETED_DLX_ROUTING_KEY)
                .build();

        private final Queue fileDeletedDlxQueue = QueueBuilder
                .durable(FILE_DELETED_DLQ_QUEUE)
                .build();

        private final Binding fileDeletedBinding = BindingBuilder
                .bind(fileDeletedQueue)
                .to(driveExchange)
                .with(FILE_DELETED_ROUTING_KEY);

        private final Binding fileDeletedDlxBinding = BindingBuilder
                .bind(fileDeletedDlxQueue)
                .to(driveDlxExchange)
                .with(FILE_DELETED_DLX_ROUTING_KEY);

        private final Queue fileSharedQueue = QueueBuilder
                .durable(FILE_SHARED_QUEUE_NAME)
                .deadLetterExchange(DRIVE_DLX_EXCHANGE_NAME)
                .deadLetterRoutingKey(FILE_SHARED_DLX_ROUTING_KEY)
                .build();

        private final Queue fileSharedDlxQueue = QueueBuilder
                .durable(FILE_SHARED_DLQ_QUEUE)
                .build();

        private final Binding fileSharedBinding = BindingBuilder
                .bind(fileSharedQueue)
                .to(driveExchange)
                .with(FILE_SHARED_ROUTING_KEY);

        private final Binding fileSharedDlxBinding = BindingBuilder
                .bind(fileSharedDlxQueue)
                .to(driveDlxExchange)
                .with(FILE_SHARED_DLX_ROUTING_KEY);

        private final Queue memberCreatedQueue = QueueBuilder
                .durable(MEMBER_CREATED_QUEUE_NAME)
                .deadLetterExchange(DRIVE_DLX_EXCHANGE_NAME)
                .deadLetterRoutingKey(MEMBER_CREATED_DLX_ROUTING_KEY)
                .build();

        private final Queue memberCreatedDlxQueue = QueueBuilder
                .durable(MEMBER_CREATED_DLQ_QUEUE)
                .build();

        private final Binding memberCreatedBinding = BindingBuilder
                .bind(memberCreatedQueue)
                .to(driveExchange)
                .with(MEMBER_CREATED_ROUTING_KEY);

        private final Binding memberCreatedDlxBinding = BindingBuilder
                .bind(memberCreatedDlxQueue)
                .to(driveDlxExchange)
                .with(MEMBER_CREATED_DLX_ROUTING_KEY);

        private final Queue memberUpdatedQueue = QueueBuilder
                .durable(MEMBER_UPDATED_QUEUE_NAME)
                .deadLetterExchange(DRIVE_DLX_EXCHANGE_NAME)
                .deadLetterRoutingKey(MEMBER_UPDATED_DLX_ROUTING_KEY)
                .build();

        private final Queue memberUpdatedDlxQueue = QueueBuilder
                .durable(MEMBER_UPDATED_DLQ_QUEUE)
                .build();

        private final Binding memberUpdatedBinding = BindingBuilder
                .bind(memberUpdatedQueue)
                .to(driveExchange)
                .with(MEMBER_UPDATED_ROUTING_KEY);

        private final Binding memberUpdatedDlxBinding = BindingBuilder
                .bind(memberUpdatedDlxQueue)
                .to(driveDlxExchange)
                .with(MEMBER_UPDATED_DLX_ROUTING_KEY);

        @Bean
        TopicExchange eventHubExchange() {
            return eventHubExchange;
        }

        @Bean
        TopicExchange driveExchange() {
            return driveExchange;
        }

        @Bean
        TopicExchange driveDlxExchange() {
            return driveDlxExchange;
        }

        @Bean
        Binding driveEventsBinding() {
            return driveEventsBinding;
        }

        @Bean
        Binding eventHubMemberCreatedEventBinding() {
            return eventHubMemberCreatedEventBinding;
        }

        @Bean
        Binding eventHubMemberUpdatedEventBinding() {
            return eventHubMemberUpdatedEventBinding;
        }

        @Bean
        Queue fileDeletedQueue() {
            return fileDeletedQueue;
        }

        @Bean
        Queue fileDeletedDlxQueue() {
            return fileDeletedDlxQueue;
        }

        @Bean
        Binding fileDeletedBinding() {
            return fileDeletedBinding;
        }

        @Bean
        Binding fileDeletedDlxBinding() {
            return fileDeletedDlxBinding;
        }

        @Bean
        Queue fileSharedQueue() {
            return fileSharedQueue;
        }

        @Bean
        Queue fileSharedDlxQueue() {
            return fileSharedDlxQueue;
        }

        @Bean
        Binding fileSharedBinding() {
            return fileSharedBinding;
        }

        @Bean
        Binding fileSharedDlxBinding() {
            return fileSharedDlxBinding;
        }

        @Bean
        Queue memberCreatedQueue() {
            return memberCreatedQueue;
        }

        @Bean
        Queue memberCreatedDlxQueue() {
            return memberCreatedDlxQueue;
        }

        @Bean
        Binding memberCreatedBinding() {
            return memberCreatedBinding;
        }

        @Bean
        Binding memberCreatedDlxBinding() {
            return memberCreatedDlxBinding;
        }

        @Bean
        Queue memberUpdatedQueue() {
            return memberUpdatedQueue;
        }

        @Bean
        Queue memberUpdatedDlxQueue() {
            return memberUpdatedDlxQueue;
        }

        @Bean
        Binding memberUpdatedBinding() {
            return memberUpdatedBinding;
        }

        @Bean
        Binding memberUpdatedDlxBinding() {
            return memberUpdatedDlxBinding;
        }

    }

}
