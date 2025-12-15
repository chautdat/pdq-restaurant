package com.pdq.service;

import com.pdq.config.RabbitMQConfig;
import com.pdq.dto.message.OrderMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendOrderCreatedMessage(OrderMessage message) {
        log.info("🐰 Publishing order created message: Order #{}", message.getOrderNumber());

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_EMAIL_ROUTING_KEY,
                    message
            );
            log.info("✅ Sent to email queue");

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_SMS_ROUTING_KEY,
                    message
            );
            log.info("✅ Sent to SMS queue");

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_ANALYTICS_ROUTING_KEY,
                    message
            );
            log.info("✅ Sent to analytics queue");

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_KITCHEN_ROUTING_KEY,
                    message
            );
            log.info("✅ Sent to kitchen queue");

            log.info("🎉 All messages sent successfully for Order #{}", message.getOrderNumber());
        } catch (Exception e) {
            log.error("❌ Error sending messages for Order #{}: {}", message.getOrderNumber(), e.getMessage());
            throw new RuntimeException("Failed to send order messages", e);
        }
    }

    public void sendOrderStatusUpdateMessage(OrderMessage message) {
        log.info("🐰 Publishing order status update: Order #{} → {}",
                message.getOrderNumber(), message.getOrderStatus());

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_EMAIL_ROUTING_KEY,
                    message
            );

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_KITCHEN_ROUTING_KEY,
                    message
            );

            log.info("✅ Status update messages sent for Order #{}", message.getOrderNumber());
        } catch (Exception e) {
            log.error("❌ Error sending status update for Order #{}: {}",
                    message.getOrderNumber(), e.getMessage());
        }
    }
}
