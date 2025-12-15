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
public class EmailConsumer {

    @RabbitListener(queues = RabbitMQConfig.ORDER_EMAIL_QUEUE)
    public void consumeOrderEmail(OrderMessage message) {
        log.info("📧 [EMAIL CONSUMER] Received message for Order #{}", message.getOrderNumber());

        try {
            Thread.sleep(2000);
            String emailContent = buildEmailContent(message);
            log.info("📧 Sending email to: {}", message.getCustomerEmail());
            log.info("📧 Subject: Xác nhận đơn hàng #{}", message.getOrderNumber());
            log.info("📧 Content:\n{}", emailContent);
            log.info("✅ Email sent successfully for Order #{}", message.getOrderNumber());
        } catch (Exception e) {
            log.error("❌ Failed to send email for Order #{}: {}",
                    message.getOrderNumber(), e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }

    private String buildEmailContent(OrderMessage message) {
        StringBuilder content = new StringBuilder();
        content.append("Xin chào ").append(message.getCustomerName()).append(",\n\n");
        content.append("Cảm ơn bạn đã đặt hàng tại PDQ Restaurant!\n\n");
        content.append("Thông tin đơn hàng:\n");
        content.append("- Mã đơn: ").append(message.getOrderNumber()).append("\n");
        content.append("- Tổng tiền: ").append(message.getTotalAmount()).append("đ\n");
        content.append("- Địa chỉ giao: ").append(message.getDeliveryAddress()).append("\n");
        content.append("- Phương thức thanh toán: ").append(message.getPaymentMethod()).append("\n\n");

        content.append("Sản phẩm:\n");
        if (message.getItems() != null) {
            message.getItems().forEach(item -> {
                content.append("  - ").append(item.getProductName())
                        .append(" x").append(item.getQuantity())
                        .append(" = ").append(item.getSubtotal()).append("đ\n");
            });
        }

        content.append("\nChúng tôi sẽ liên hệ với bạn sớm nhất!\n");
        content.append("Trân trọng,\nPDQ Restaurant Team");

        return content.toString();
    }
}
