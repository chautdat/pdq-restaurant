package com.pdq.consumer;

import com.pdq.config.RabbitMQConfig;
import com.pdq.dto.message.OrderMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnalyticsConsumer {

    @RabbitListener(queues = RabbitMQConfig.ORDER_ANALYTICS_QUEUE)
    public void consumeOrderAnalytics(OrderMessage message) {
        log.info("📊 [ANALYTICS CONSUMER] Received message for Order #{}", message.getOrderNumber());

        try {
            Thread.sleep(500);

            log.info("📊 Processing analytics for Order #{}:", message.getOrderNumber());
            log.info("   - Customer ID: {}", message.getUserId());
            log.info("   - Total Amount: {}", message.getTotalAmount());
            log.info("   - Payment Method: {}", message.getPaymentMethod());
            log.info("   - Items Count: {}", message.getItems() != null ? message.getItems().size() : 0);

            if (message.getItems() != null && !message.getItems().isEmpty()) {
                BigDecimal avgItemPrice = message.getTotalAmount()
                        .divide(BigDecimal.valueOf(message.getItems().size()), 2, BigDecimal.ROUND_HALF_UP);
                log.info("   - Average Item Price: {}", avgItemPrice);
            }

            log.info("✅ Analytics processed for Order #{}", message.getOrderNumber());
        } catch (Exception e) {
            log.error("❌ Failed to process analytics for Order #{}: {}",
                    message.getOrderNumber(), e.getMessage());
        }
    }
}
