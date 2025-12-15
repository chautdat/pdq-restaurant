package com.pdq.consumer;

import com.pdq.config.RabbitMQConfig;
import com.pdq.dto.message.OrderMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KitchenConsumer {

    @RabbitListener(queues = RabbitMQConfig.ORDER_KITCHEN_QUEUE)
    public void consumeKitchenOrder(OrderMessage message) {
        log.info("👨‍🍳 [KITCHEN CONSUMER] Received message for Order #{}", message.getOrderNumber());

        try {
            Thread.sleep(300);

            log.info("👨‍🍳 ========================================");
            log.info("👨‍🍳 NEW ORDER - Order #{}", message.getOrderNumber());
            log.info("👨‍🍳 ========================================");
            log.info("👨‍🍳 Customer: {}", message.getCustomerName());
            log.info("👨‍🍳 Phone: {}", message.getCustomerPhone());
            log.info("👨‍🍳 Status: {}", message.getOrderStatus());
            log.info("👨‍🍳 ----------------------------------------");

            if (message.getItems() != null) {
                log.info("👨‍🍳 ITEMS TO PREPARE:");
                message.getItems().forEach(item -> {
                    log.info("👨‍🍳   [{}x] {}", item.getQuantity(), item.getProductName());
                });
            }

            log.info("👨‍🍳 ========================================");
            log.info("✅ Kitchen notification sent for Order #{}", message.getOrderNumber());
        } catch (Exception e) {
            log.error("❌ Failed to process kitchen notification for Order #{}: {}",
                    message.getOrderNumber(), e.getMessage());
        }
    }
}
