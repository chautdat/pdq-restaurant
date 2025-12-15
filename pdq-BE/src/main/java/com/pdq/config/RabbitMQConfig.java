package com.pdq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EMAIL_QUEUE = "order.email.queue";
    public static final String ORDER_SMS_QUEUE = "order.sms.queue";
    public static final String ORDER_ANALYTICS_QUEUE = "order.analytics.queue";
    public static final String ORDER_KITCHEN_QUEUE = "order.kitchen.queue";

    public static final String ORDER_EXCHANGE = "order.exchange";

    public static final String ORDER_EMAIL_ROUTING_KEY = "order.email";
    public static final String ORDER_SMS_ROUTING_KEY = "order.sms";
    public static final String ORDER_ANALYTICS_ROUTING_KEY = "order.analytics";
    public static final String ORDER_KITCHEN_ROUTING_KEY = "order.kitchen";

    @Bean
    public Queue orderEmailQueue() {
        return QueueBuilder.durable(ORDER_EMAIL_QUEUE)
                .withArgument("x-message-ttl", 3_600_000)
                .build();
    }

    @Bean
    public Queue orderSmsQueue() {
        return QueueBuilder.durable(ORDER_SMS_QUEUE)
                .withArgument("x-message-ttl", 3_600_000)
                .build();
    }

    @Bean
    public Queue orderAnalyticsQueue() {
        return QueueBuilder.durable(ORDER_ANALYTICS_QUEUE)
                .withArgument("x-message-ttl", 86_400_000)
                .build();
    }

    @Bean
    public Queue orderKitchenQueue() {
        return QueueBuilder.durable(ORDER_KITCHEN_QUEUE)
                .withArgument("x-message-ttl", 1_800_000)
                .build();
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(orderEmailQueue())
                .to(orderExchange())
                .with(ORDER_EMAIL_ROUTING_KEY);
    }

    @Bean
    public Binding smsBinding() {
        return BindingBuilder.bind(orderSmsQueue())
                .to(orderExchange())
                .with(ORDER_SMS_ROUTING_KEY);
    }

    @Bean
    public Binding analyticsBinding() {
        return BindingBuilder.bind(orderAnalyticsQueue())
                .to(orderExchange())
                .with(ORDER_ANALYTICS_ROUTING_KEY);
    }

    @Bean
    public Binding kitchenBinding() {
        return BindingBuilder.bind(orderKitchenQueue())
                .to(orderExchange())
                .with(ORDER_KITCHEN_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
