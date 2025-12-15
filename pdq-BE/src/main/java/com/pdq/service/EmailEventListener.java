package com.pdq.service;

import com.pdq.entity.OrderStatus;
import com.pdq.event.OrderCreatedEvent;
import com.pdq.event.OrderPaidEvent;
import com.pdq.event.OrderStatusChangedEvent;
import com.pdq.event.PasswordResetRequestedEvent;
import com.pdq.event.UserRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailEventListener {

    private static final Logger log = LoggerFactory.getLogger(EmailEventListener.class);

    private final OrderEmailService orderEmailService;
    private final PaymentEmailService paymentEmailService;
    private final OrderStatusEmailService orderStatusEmailService;
    private final UserEmailService userEmailService;

    public EmailEventListener(OrderEmailService orderEmailService,
                              PaymentEmailService paymentEmailService,
                              OrderStatusEmailService orderStatusEmailService,
                              UserEmailService userEmailService) {
        this.orderEmailService = orderEmailService;
        this.paymentEmailService = paymentEmailService;
        this.orderStatusEmailService = orderStatusEmailService;
        this.userEmailService = userEmailService;
    }

    @Async
    @EventListener
    public void onOrderCreated(OrderCreatedEvent event) {
        try {
            orderEmailService.sendOrderPlacedSuccess(event.getOrder());
            orderEmailService.sendNewOrderAlert(event.getOrder());
        } catch (Exception e) {
            log.error("❌ onOrderCreated email error", e);
        }
    }

    @Async
    @EventListener
    public void onOrderPaid(OrderPaidEvent event) {
        try {
            paymentEmailService.sendPaymentSuccessToCustomer(event.getOrder(), event.getPaymentMethod());
            paymentEmailService.sendPaymentSuccessToAdmin(event.getOrder(), event.getPaymentMethod());
        } catch (Exception e) {
            log.error("❌ onOrderPaid email error", e);
        }
    }

    @Async
    @EventListener
    public void onOrderStatusChanged(OrderStatusChangedEvent event) {
        try {
            orderStatusEmailService.sendStatusUpdateEmail(
                    event.getOrder(),
                    event.getOldStatus(),
                    event.getNewStatus()
            );

            if (event.getNewStatus() == OrderStatus.delivered) {
                orderEmailService.sendDeliveryCompleted(event.getOrder());
            }
        } catch (Exception e) {
            log.error("❌ onOrderStatusChanged email error", e);
        }
    }

    @Async
    @EventListener
    public void onUserRegistered(UserRegisteredEvent event) {
        try {
            // You can pass verifyLink generated elsewhere; fallback welcome email for now
            userEmailService.sendWelcomeEmail(event.getUser());
        } catch (Exception e) {
            log.error("❌ onUserRegistered email error", e);
        }
    }

    @Async
    @EventListener
    public void onPasswordResetRequested(PasswordResetRequestedEvent event) {
        try {
            userEmailService.sendResetPasswordEmail(event.getEmail(), event.getResetLink());
        } catch (Exception e) {
            log.error("❌ onPasswordResetRequested email error", e);
        }
    }
}
