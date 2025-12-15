package com.pdq.event;

import com.pdq.entity.Order;
import com.pdq.entity.OrderStatus;

public class OrderStatusChangedEvent {
    private final Order order;
    private final OrderStatus oldStatus;
    private final OrderStatus newStatus;

    public OrderStatusChangedEvent(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        this.order = order;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }

    public Order getOrder() {
        return order;
    }

    public OrderStatus getOldStatus() {
        return oldStatus;
    }

    public OrderStatus getNewStatus() {
        return newStatus;
    }
}
