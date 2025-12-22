-- Create promo_codes table for promotional discount codes
CREATE TABLE promo_codes (
    promo_code_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE COMMENT 'Promotional code (e.g., SUMMER50, SIGNUP_123)',
    description VARCHAR(255) COMMENT 'Description of the promo code',
    discount_type ENUM('PERCENTAGE', 'FIXED_AMOUNT', 'FREE_SHIPPING') NOT NULL COMMENT 'Type of discount',
    discount_value DECIMAL(10, 2) NOT NULL COMMENT 'Discount value based on type (50 for 50%, 50000 for 50k VND)',
    minimum_order_amount DECIMAL(10, 2) DEFAULT 0 COMMENT 'Minimum order amount required to apply promo',
    max_discount_amount DECIMAL(10, 2) COMMENT 'Maximum discount amount for percentage type',
    active TINYINT(1) NOT NULL DEFAULT 1 COMMENT 'Whether the promo code is active',
    usage_limit INT COMMENT 'Maximum number of times this code can be used (NULL = unlimited)',
    usage_count INT NOT NULL DEFAULT 0 COMMENT 'Number of times this code has been used',
    used_by_users LONGTEXT COMMENT 'CSV list of user IDs who have used this code',
    start_date DATETIME COMMENT 'Start date when promo becomes active',
    end_date DATETIME COMMENT 'End date when promo expires',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    created_by VARCHAR(100) COMMENT 'Admin user who created this promo code',
    
    KEY idx_code (code),
    KEY idx_active (active),
    KEY idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Promotional discount codes for users';
