package com.pdq.event;

import com.pdq.entity.Order;
import com.pdq.entity.PaymentMethod;

public class OrderPaidEvent {
    private final Order order;
    private final PaymentMethod paymentMethod;

    public OrderPaidEvent(Order order, PaymentMethod paymentMethod) {
        this.order = order;
        this.paymentMethod = paymentMethod;
    }

    public Order getOrder() {
        return order;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
}
