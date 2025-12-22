-- Add promo_code column to orders table to track applied promotional codes
ALTER TABLE orders ADD COLUMN promo_code VARCHAR(50) COMMENT 'Applied promotional code' AFTER discount_amount;
