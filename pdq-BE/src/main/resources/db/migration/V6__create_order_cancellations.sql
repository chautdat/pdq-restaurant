CREATE TABLE IF NOT EXISTS order_cancellations (
    cancellation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE,
    cancelled_by BIGINT NOT NULL,
    reason VARCHAR(255) NOT NULL,
    additional_notes TEXT,
    refund_status VARCHAR(50) DEFAULT 'PENDING',
    refund_amount DOUBLE,
    cancelled_at DATETIME NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (cancelled_by) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_order_id (order_id),
    INDEX idx_cancelled_by (cancelled_by)
);
