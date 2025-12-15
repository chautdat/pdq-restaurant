CREATE TABLE IF NOT EXISTS payment_logs (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    payment_method VARCHAR(50),
    payment_status VARCHAR(50),
    transaction_id VARCHAR(255),
    amount DECIMAL(10, 2),
    retry_count INT DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    INDEX idx_order_id (order_id),
    INDEX idx_transaction_id (transaction_id)
);

ALTER TABLE orders 
ADD COLUMN payment_expires_at DATETIME,
ADD COLUMN retry_count INT DEFAULT 0,
ADD COLUMN max_retries INT DEFAULT 1;
