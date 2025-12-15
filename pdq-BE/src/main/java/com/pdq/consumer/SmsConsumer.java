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
public class SmsConsumer {

    @RabbitListener(queues = RabbitMQConfig.ORDER_SMS_QUEUE)
    public void consumeOrderSms(OrderMessage message) {
        log.info("📱 [SMS CONSUMER] Received message for Order #{}", message.getOrderNumber());

        try {
            Thread.sleep(1000);
            String smsContent = buildSmsContent(message);
            log.info("📱 Sending SMS to: {}", message.getCustomerPhone());
            log.info("📱 Content: {}", smsContent);
            log.info("✅ SMS sent successfully for Order #{}", message.getOrderNumber());
        } catch (Exception e) {
            log.error("❌ Failed to send SMS for Order #{}: {}",
                    message.getOrderNumber(), e.getMessage());
            throw new RuntimeException("SMS sending failed", e);
        }
    }

    private String buildSmsContent(OrderMessage message) {
        return String.format(
                "PDQ Restaurant: Don hang #%s da duoc tiep nhan. Tong tien: %sđ. Cam on quy khach!",
                message.getOrderNumber(),
                message.getTotalAmount()
        );
    }
}
