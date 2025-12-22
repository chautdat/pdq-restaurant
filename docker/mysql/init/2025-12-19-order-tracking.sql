-- Add order tracking geo fields
ALTER TABLE orders
    ADD COLUMN IF NOT EXISTS delivery_lat DECIMAL(10,8) COMMENT 'Latitude địa chỉ giao hàng',
    ADD COLUMN IF NOT EXISTS delivery_lng DECIMAL(11,8) COMMENT 'Longitude địa chỉ giao hàng',
    ADD COLUMN IF NOT EXISTS distance DECIMAL(5,2) COMMENT 'Khoảng cách (km)',
    ADD COLUMN IF NOT EXISTS duration INT COMMENT 'Thời gian dự kiến (phút)',
    ADD COLUMN IF NOT EXISTS estimated_arrival DATETIME COMMENT 'Giờ dự kiến giao hàng';

CREATE INDEX IF NOT EXISTS idx_delivery_location ON orders(delivery_lat, delivery_lng);
CREATE INDEX IF NOT EXISTS idx_estimated_arrival ON orders(estimated_arrival);
