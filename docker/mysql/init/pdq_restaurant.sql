-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Dec 04, 2025 at 06:38 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pdq_restaurant`
--

-- --------------------------------------------------------

--
-- Table structure for table `addresses`
--

CREATE TABLE `addresses` (
  `address_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `recipient_name` varchar(100) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `address_line` varchar(255) NOT NULL,
  `ward` varchar(100) DEFAULT NULL,
  `district` varchar(100) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `is_default` tinyint(1) NOT NULL DEFAULT 0,
  `address_type` enum('home','office','other') DEFAULT 'home',
  `note` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `addresses`
--

INSERT INTO `addresses` (`address_id`, `user_id`, `recipient_name`, `phone`, `address_line`, `ward`, `district`, `city`, `is_default`, `address_type`, `note`, `created_at`, `updated_at`) VALUES
(1, 4, 'Chau Tan Dat', '0984905718', '15 Lo s Nguyen Thi Tan', 'Phường 2', 'Quận 8', 'Hồ chí minh', 0, 'home', '', '2025-11-25 20:03:50', '2025-11-25 20:03:50');

-- --------------------------------------------------------

--
-- Table structure for table `carts`
--

CREATE TABLE `carts` (
  `cart_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `total_items` int(11) NOT NULL DEFAULT 0,
  `total_price` decimal(10,2) NOT NULL DEFAULT 0.00,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `carts`
--

INSERT INTO `carts` (`cart_id`, `user_id`, `total_items`, `total_price`, `created_at`, `updated_at`) VALUES
(1, 4, 0, 0.00, '2025-11-21 17:09:35', '2025-11-21 17:09:35'),
(2, 1, 0, 0.00, '2025-11-22 09:31:40', '2025-11-22 09:31:40');

-- --------------------------------------------------------

--
-- Table structure for table `cart_items`
--

CREATE TABLE `cart_items` (
  `cart_item_id` bigint(20) NOT NULL,
  `cart_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT 1,
  `price` decimal(10,2) NOT NULL,
  `notes` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `discount_price` decimal(10,2) DEFAULT NULL,
  `subtotal` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `cart_items`
--

INSERT INTO `cart_items` (`cart_item_id`, `cart_id`, `product_id`, `quantity`, `price`, `notes`, `created_at`, `updated_at`, `discount_price`, `subtotal`) VALUES
(56, 1, 37, 1, 55000.00, '', '2025-12-01 20:18:40', '2025-12-03 08:55:45', 0.00, 55000.00);

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `category_name` varchar(100) NOT NULL,
  `category_slug` varchar(120) NOT NULL,
  `description` text DEFAULT NULL,
  `image_url` varchar(500) DEFAULT NULL,
  `display_order` int(11) DEFAULT 0,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`category_id`, `category_name`, `category_slug`, `description`, `image_url`, `display_order`, `is_active`, `created_at`, `updated_at`) VALUES
(1, 'Món chính', 'mon-chinh', 'Các món ăn chính như cơm, phở, bún...', NULL, 1, 1, '2025-11-21 10:33:02', '2025-11-21 10:33:02'),
(2, 'Món phụ', 'mon-phu', 'Các món ăn phụ, ăn kèm', NULL, 2, 1, '2025-11-21 10:33:02', '2025-11-21 10:33:02'),
(3, 'Đồ uống', 'do-uong', 'Nước ngọt, trà, cà phê, sinh tố...', NULL, 3, 1, '2025-11-21 10:33:02', '2025-11-21 10:33:02'),
(4, 'Tráng miệng', 'trang-mieng', 'Các món tráng miệng, bánh ngọt...', NULL, 4, 1, '2025-11-21 10:33:02', '2025-11-21 10:33:02'),
(5, 'Topping', 'topping', 'Các loại topping thêm', NULL, 5, 1, '2025-11-21 10:33:02', '2025-11-21 10:33:02');

-- --------------------------------------------------------

--
-- Table structure for table `favorites`
--

CREATE TABLE `favorites` (
  `favorite_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `notification_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `title` varchar(200) NOT NULL,
  `message` text NOT NULL,
  `type` varchar(50) NOT NULL,
  `reference_id` bigint(20) DEFAULT NULL COMMENT 'ID đơn hàng, sản phẩm, etc',
  `is_read` tinyint(1) NOT NULL DEFAULT 0,
  `read_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `action_url` varchar(500) DEFAULT NULL,
  `image_url` varchar(500) DEFAULT NULL,
  `reference_type` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `order_number` varchar(50) NOT NULL,
  `recipient_name` varchar(100) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `delivery_address` varchar(500) NOT NULL,
  `total_amount` decimal(38,2) NOT NULL,
  `discount_amount` decimal(10,2) DEFAULT 0.00,
  `shipping_fee` decimal(10,2) DEFAULT 0.00,
  `final_amount` decimal(10,2) NOT NULL,
  `payment_method` enum('cash','vnpay','momo','bank_transfer') NOT NULL DEFAULT 'cash',
  `payment_status` enum('pending','paid','failed','refunded') NOT NULL DEFAULT 'pending',
  `order_status` enum('pending','confirmed','preparing','ready','delivering','delivered','cancelled') NOT NULL,
  `notes` text DEFAULT NULL,
  `cancelled_reason` varchar(500) DEFAULT NULL,
  `cancelled_at` datetime DEFAULT NULL,
  `confirmed_at` datetime DEFAULT NULL,
  `delivered_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `address_line` varchar(255) NOT NULL,
  `subtotal` decimal(38,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `user_id`, `order_number`, `recipient_name`, `phone`, `delivery_address`, `total_amount`, `discount_amount`, `shipping_fee`, `final_amount`, `payment_method`, `payment_status`, `order_status`, `notes`, `cancelled_reason`, `cancelled_at`, `confirmed_at`, `delivered_at`, `created_at`, `updated_at`, `address_line`, `subtotal`) VALUES
(66, 4, 'ORD202511281843327969', 'Châu Tấn ', '0984905718', '15 Lo  s nguiyen thi  tan phuong 2 quan 8', 120000.00, 0.00, 15000.00, 120000.00, 'vnpay', 'paid', 'delivered', NULL, NULL, NULL, '2025-11-28 18:45:22', '2025-11-28 19:07:34', '2025-11-28 18:43:32', '2025-11-29 11:00:04', '15 Lo  s nguiyen thi  tan phuong 2 quan 8', 105000.00),
(67, 4, 'ORD202512012011478706', 'Châu Tấn ', '0984905718', '15 lo s nguyen thi  tan phuong 2 quan  8', 190000.00, 0.00, 15000.00, 190000.00, 'cash', 'pending', 'pending', NULL, NULL, NULL, NULL, NULL, '2025-12-01 20:11:47', '2025-12-01 20:11:47', '15 lo s nguyen thi  tan phuong 2 quan  8', 175000.00);

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `order_item_id` bigint(20) NOT NULL,
  `order_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `product_name` varchar(200) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `discount_price` decimal(10,2) DEFAULT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  `notes` varchar(500) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `product_image` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`order_item_id`, `order_id`, `product_id`, `product_name`, `quantity`, `price`, `discount_price`, `subtotal`, `notes`, `created_at`, `product_image`) VALUES
(112, 66, 37, 'Cơm Chiên Dương Châu', 1, 55000.00, NULL, 55000.00, NULL, '2025-11-28 18:43:32.000000', '/uploads/f5a2d5dc-af72-4155-8a39-77d9b955fb6d.png'),
(113, 66, 36, 'Cơm Gà Hải Nam', 1, 50000.00, NULL, 50000.00, NULL, '2025-11-28 18:43:32.000000', '/uploads/ed4c44f0-4b30-467b-b702-bf56b9dcb327.png'),
(114, 67, 37, 'Cơm Chiên Dương Châu', 1, 55000.00, NULL, 55000.00, NULL, '2025-12-01 20:11:47.000000', '/uploads/f5a2d5dc-af72-4155-8a39-77d9b955fb6d.png'),
(115, 67, 35, 'Bún Chả Hà Nội', 2, 60000.00, NULL, 120000.00, NULL, '2025-12-01 20:11:47.000000', '/uploads/ed2c36da-9fab-4427-817b-56215d808fbd.png');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `product_id` bigint(20) NOT NULL,
  `category_id` int(11) NOT NULL,
  `product_name` varchar(200) NOT NULL,
  `product_slug` varchar(220) NOT NULL,
  `description` text DEFAULT NULL,
  `short_description` varchar(500) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `discount_price` decimal(10,2) DEFAULT NULL,
  `stock_quantity` int(11) NOT NULL DEFAULT 0,
  `image_url` varchar(500) DEFAULT NULL,
  `sku` varchar(50) DEFAULT NULL,
  `unit` varchar(20) DEFAULT 'phần',
  `preparation_time` int(11) DEFAULT NULL COMMENT 'Thời gian chuẩn bị (phút)',
  `is_featured` tinyint(1) NOT NULL DEFAULT 0,
  `is_available` tinyint(1) NOT NULL DEFAULT 1,
  `average_rating` decimal(3,2) DEFAULT 0.00,
  `review_count` int(11) DEFAULT 0,
  `sold_count` int(11) DEFAULT 0,
  `view_count` int(11) DEFAULT 0,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `category_id`, `product_name`, `product_slug`, `description`, `short_description`, `price`, `discount_price`, `stock_quantity`, `image_url`, `sku`, `unit`, `preparation_time`, `is_featured`, `is_available`, `average_rating`, `review_count`, `sold_count`, `view_count`, `created_at`, `updated_at`) VALUES
(22, 1, 'Cơm Tấm Sườn Bì Chả', 'com-tam-suon-bi-cha', 'Cơm tấm ăn cùng sườn nướng vàng ươm, bì trộn thính, chả trứng béo nhẹ và mỡ hành. Kèm đồ chua, cà chua và dưa leo giúp món ăn thêm tròn vị.', 'Cơm tấm chuẩn vị Sài Gòn, sườn mềm và thơm.', 55000.00, NULL, 100, '/uploads/18a161fa-b58d-4cc6-9647-7c062060516a.png', NULL, 'phần', 15, 0, 1, 0.00, 0, 0, 0, '2025-11-25 19:31:36', '2025-11-26 18:46:25'),
(23, 1, 'Phở Bò Tái Nạm', 'pho-bo-tai-nam', 'Phở bò với nước dùng trong, thơm ngọt từ xương hầm nhiều giờ. Thịt bò tái – nạm mềm, ăn kèm hành lá và rau thơm tạo nên hương vị đậm đà.', 'Phở bò tái nạm thơm ngon, nước trong ngọt vị.', 60000.00, NULL, 100, '/uploads/abeeae71-79ee-4293-b39b-00ca5e4ac075.png', NULL, 'phần', 15, 0, 1, 0.00, 0, 0, 2, '2025-11-25 19:32:05', '2025-11-27 15:48:14'),
(24, 2, 'Chả Giò Chiên', 'cha-gio-chien', 'Chả giò được cuốn từ thịt, miến, cà rốt và mộc nhĩ, sau đó chiên vàng giòn. Lớp vỏ ngoài giòn rụm, nhân bên trong đậm vị, ăn kèm tương ớt hoặc nước mắm chua ngọt rất hài hòa.', 'Chả giò chiên giòn rụm, nhân thơm đậm đà.', 35000.00, NULL, 100, '/uploads/cf4c9874-4b52-4a37-a75f-5f9693019113.png', NULL, 'phần', 15, 0, 1, 0.00, 0, 0, 0, '2025-11-25 19:34:41', '2025-11-30 20:16:05'),
(25, 2, 'Đậu Hũ Chiên Giòn', 'dau-hu-chien-gion', 'Đậu hũ tươi được cắt miếng và chiên vàng đều, bên ngoài giòn nhẹ, bên trong mềm mịn. Thích hợp dùng kèm nước tương hoặc mắm gừng, là món ăn nhẹ ngon miệng và dễ ăn.', 'Đậu hũ chiên vàng giòn, mềm bên trong.', 25000.00, NULL, 100, '/uploads/36143b26-55d1-42a0-b0c3-889ff99888c6.png', NULL, 'phần', 15, 0, 1, 0.00, 0, 0, 2, '2025-11-25 19:35:15', '2025-11-27 15:48:13'),
(26, 2, 'Gỏi Cuốn Tôm Thịt', 'goi-cuon-tom-thit', 'Gỏi cuốn với tôm, thịt heo, bún tươi, xà lách và rau thơm được cuốn trong bánh tráng mềm. Ăn kèm nước chấm đậu phộng tạo nên hương vị thanh mát và hấp dẫn.', 'Gỏi cuốn tươi mát, thanh đạm.', 30000.00, NULL, 100, '/uploads/1449aab1-af76-42e0-a6f2-b08b5de36281.png', NULL, 'phần', 15, 0, 1, 0.00, 0, 0, 2, '2025-11-25 19:35:49', '2025-11-27 15:48:13'),
(27, 4, 'Chè Ba Màu', 'che-ba-mau', 'Chè ba màu gồm lớp xanh, trắng và đỏ đẹp mắt, ăn cùng nước cốt dừa và đá bào. Vị ngọt thanh, béo nhẹ, thích hợp giải nhiệt ngày nóng.', 'Chè ba màu mát lạnh, béo nhẹ, ngọt dịu.', 25000.00, NULL, 100, '/uploads/6e62d705-9e95-4763-bedf-08f1fa47e2e1.png', NULL, 'phần', 15, 0, 1, 0.00, 0, 0, 2, '2025-11-25 19:37:13', '2025-11-27 15:48:13'),
(28, 4, 'Chè Khúc Bạch', 'che-khuc-bach', 'Chè khúc bạch gồm nhiều loại khúc bạch như vị trà xanh, sữa, cacao, ăn kèm nhãn, hạnh nhân lát và nước đường thanh mát. Là món tráng miệng được yêu thích nhờ vị giòn – béo – thơm đặc trưng.', 'Chè khúc bạch giòn nhẹ, thơm béo, mát lạnh.', 30000.00, NULL, 100, '/uploads/ec5bad71-78b2-41b4-9347-07112ef76a5e.png', NULL, 'phần', 15, 0, 1, 0.00, 0, 0, 0, '2025-11-25 19:37:44', '2025-11-26 18:46:25'),
(29, 4, 'Bánh Flan', 'banh-flan', 'Bánh flan được làm từ trứng, sữa và caramel. Kết cấu mềm mịn, vị béo nhẹ hòa quyện với lớp caramel ngọt đắng tạo nên món tráng miệng thanh nhẹ, dễ ăn.', 'Flan mềm mịn, béo nhẹ, phủ caramel đậm vị.', 25000.00, NULL, 100, '/uploads/69a9a0ab-18c3-4e46-8008-00b137cc55d8.png', NULL, 'phần', 15, 0, 1, 0.00, 0, 0, 2, '2025-11-25 19:38:13', '2025-11-27 15:48:18'),
(30, 5, 'Chả Lụa', 'cha-lua', 'Chả lụa được làm từ thịt heo xay và nước mắm, gói lá chuối và hấp chín. Miếng chả dai mềm, thơm nhẹ vị tiêu, rất thích hợp dùng kèm bún, phở, cơm tấm hoặc ăn riêng.', 'Chả lụa dai ngon, thơm nhẹ, phù hợp ăn kèm nhiều món.', 15000.00, NULL, 100, '/uploads/78fc9a42-966b-466d-a48f-ba767292c900.png', NULL, 'phần', 15, 0, 1, 0.00, 0, 0, 4, '2025-11-25 19:39:51', '2025-11-28 18:42:36'),
(31, 5, 'Trứng Ốp La', 'trung-op-la', 'Trứng được chiên trên lửa vừa để lòng trắng chín mềm, còn lòng đỏ giữ độ sệt vừa phải. Hương vị béo nhẹ, thơm và rất hợp khi ăn cùng cơm tấm, mì, bánh mì hoặc topping thêm cho món chính.', 'Trứng ốp la vàng ươm, lòng đào hấp dẫn.', 10000.00, NULL, 100, '/uploads/44816af8-965d-44db-a1fb-3b781739ce0f.png', NULL, 'phần', 15, 0, 1, 0.00, 0, 0, 0, '2025-11-25 19:40:31', '2025-11-26 18:46:25'),
(32, 3, 'Cà Phê Sữa Đá', 'ca-phe-sua-da', 'Cà phê phin truyền thống pha cùng sữa đặc, sau đó cho đá để tạo nên vị đậm đà đặc trưng. Lớp cà phê hòa quyện với sữa tạo vị béo, thơm, ngọt vừa phải, là thức uống quen thuộc của người Việt.', 'Cà phê sữa đá đậm vị, thơm béo, giải nhiệt.', 25000.00, NULL, 100, '/uploads/a2858fd3-7886-4000-bb5c-ef63b1b60f30.png', NULL, 'phần', 15, 0, 1, 0.00, 0, 0, 2, '2025-11-25 19:41:52', '2025-11-26 18:46:25'),
(33, 3, 'Trà Sữa Trân Châu Đường Đen', 'tra-sua-tran-chau-duong-den', 'Trà sữa được pha từ trà đen và sữa bột tạo vị thơm béo, kết hợp trân châu nấu bằng đường đen giúp món uống vừa ngọt, vừa dẻo thơm. Hòa quyện hoàn hảo giữa vị trà và độ ngọt caramel của đường đen.', 'Trà Sữa Trân Châu Đường Đen', 30000.00, NULL, 100, '/uploads/cafb67b0-3267-4572-b24d-411a7846fc20.png', NULL, 'phần', 15, 0, 1, 0.00, 0, 0, 8, '2025-11-25 19:42:29', '2025-11-28 18:42:39'),
(34, 1, 'Bún Bò Huế', 'bun-bo-hue', 'Bún Bò Huế với nước dùng thơm đậm đà, kết hợp thịt bò, chả bò và rau sống. Nước lèo ngọt xương, thêm chút sa tế tạo vị cay nhẹ hấp dẫn.', 'Bún Bò Huế đậm vị, cay nhẹ.', 55000.00, NULL, 100, '/uploads/ed02a88c-24b9-41b7-8fef-171a87b74e36.png', NULL, 'phần', 15, 0, 1, 0.00, 0, 0, 4, '2025-11-25 19:46:18', '2025-11-28 18:42:39'),
(35, 1, 'Bún Chả Hà Nội', 'bun-cha-ha-noi', 'Bún tươi ăn kèm chả nướng than, thịt ba chỉ nướng, nước mắm pha chua ngọt và rau sống. Món ăn hài hòa giữa vị mặn – ngọt – thơm của thịt nướng.', 'Bún chả Hà Nội chuẩn vị, thịt nướng thơm ngon.', 60000.00, NULL, 98, '/uploads/ed2c36da-9fab-4427-817b-56215d808fbd.png', NULL, 'phần', 15, 0, 1, 0.00, 0, 2, 8, '2025-11-25 19:56:47', '2025-12-01 20:11:47'),
(36, 1, 'Cơm Gà Hải Nam', 'com-ga-hai-nam', 'Cơm được nấu bằng nước luộc gà, hạt cơm tơi và thơm nhẹ. Thịt gà luộc mềm, ăn kèm nước chấm gừng và rau củ thanh mát.', 'Cơm gà Hải Nam mềm, thơm, béo nhẹ.', 50000.00, NULL, 99, '/uploads/ed4c44f0-4b30-467b-b702-bf56b9dcb327.png', NULL, 'phần', 15, 0, 1, 0.00, 0, 1, 14, '2025-11-25 19:57:38', '2025-11-28 18:43:32'),
(37, 1, 'Cơm Chiên Dương Châu', 'com-chien-duong-chau', 'Cơm Chiên Dương Châu nổi tiếng với hạt cơm tơi, vàng đều, được chiên cùng trứng, tôm, lạp xưởng và rau củ. Món ăn có hương vị hài hòa, thơm ngon và phù hợp cho mọi bữa ăn trong ngày.', 'Cơm chiên vàng thơm, hạt tơi, kết hợp tôm, trứng và rau củ.', 55000.00, NULL, 98, '/uploads/f5a2d5dc-af72-4155-8a39-77d9b955fb6d.png', NULL, 'phần', 15, 0, 1, 0.00, 0, 2, 52, '2025-11-25 19:59:13', '2025-12-02 16:53:39');

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews` (
  `review_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `rating` int(11) NOT NULL CHECK (`rating` >= 1 and `rating` <= 5),
  `comment` text DEFAULT NULL,
  `status` enum('pending','approved','rejected') NOT NULL DEFAULT 'pending',
  `admin_reply` text DEFAULT NULL,
  `replied_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` bigint(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `avatar_url` varchar(500) DEFAULT NULL,
  `role` enum('ROLE_CUSTOMER','ROLE_ADMIN') NOT NULL DEFAULT 'ROLE_CUSTOMER',
  `status` enum('active','inactive','banned') NOT NULL DEFAULT 'active',
  `email_verified_at` datetime DEFAULT NULL,
  `last_login_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `email`, `password`, `full_name`, `phone`, `avatar_url`, `role`, `status`, `email_verified_at`, `last_login_at`, `created_at`, `updated_at`) VALUES
(1, 'admin', 'admin@pdq.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin PDQ', '0912345678', NULL, 'ROLE_ADMIN', 'active', NULL, NULL, '2025-11-21 10:33:02', '2025-11-21 10:33:02'),
(2, 'customer', 'customer@test.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Khách Hàng Test', '0987654321', NULL, 'ROLE_CUSTOMER', 'active', NULL, NULL, '2025-11-21 10:33:02', '2025-11-25 21:31:25'),
(4, 'Châu Tấn Đạt', 'chautdat1109@gmail.com', '$2a$10$vQ2XbJ.1fjktmtQWeANK5uajuEkKMWCvLuJhwCD0at1olUpEGHQsq', 'Châu Tấn ', '', NULL, 'ROLE_CUSTOMER', 'active', '2025-11-21 15:23:59', NULL, '2025-11-21 15:23:59', '2025-11-25 12:52:08');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `addresses`
--
ALTER TABLE `addresses`
  ADD PRIMARY KEY (`address_id`),
  ADD KEY `idx_user` (`user_id`),
  ADD KEY `idx_default` (`is_default`);

--
-- Indexes for table `carts`
--
ALTER TABLE `carts`
  ADD PRIMARY KEY (`cart_id`),
  ADD UNIQUE KEY `user_id` (`user_id`),
  ADD KEY `idx_user` (`user_id`);

--
-- Indexes for table `cart_items`
--
ALTER TABLE `cart_items`
  ADD PRIMARY KEY (`cart_item_id`),
  ADD UNIQUE KEY `unique_cart_product` (`cart_id`,`product_id`),
  ADD KEY `idx_cart` (`cart_id`),
  ADD KEY `idx_product` (`product_id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`),
  ADD UNIQUE KEY `category_slug` (`category_slug`),
  ADD KEY `idx_slug` (`category_slug`),
  ADD KEY `idx_active` (`is_active`);

--
-- Indexes for table `favorites`
--
ALTER TABLE `favorites`
  ADD PRIMARY KEY (`favorite_id`),
  ADD UNIQUE KEY `unique_user_product` (`user_id`,`product_id`),
  ADD UNIQUE KEY `UKgh1s14hhb9qb8p2do933hscsf` (`user_id`,`product_id`),
  ADD KEY `idx_user` (`user_id`),
  ADD KEY `idx_product` (`product_id`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`notification_id`),
  ADD KEY `idx_user` (`user_id`),
  ADD KEY `idx_read` (`is_read`),
  ADD KEY `idx_created` (`created_at`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD UNIQUE KEY `order_number` (`order_number`),
  ADD KEY `idx_user` (`user_id`),
  ADD KEY `idx_order_number` (`order_number`),
  ADD KEY `idx_status` (`order_status`),
  ADD KEY `idx_payment_status` (`payment_status`),
  ADD KEY `idx_created` (`created_at`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`order_item_id`),
  ADD KEY `idx_order` (`order_id`),
  ADD KEY `idx_product` (`product_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD UNIQUE KEY `product_slug` (`product_slug`),
  ADD UNIQUE KEY `sku` (`sku`),
  ADD KEY `idx_category` (`category_id`),
  ADD KEY `idx_slug` (`product_slug`),
  ADD KEY `idx_price` (`price`),
  ADD KEY `idx_featured` (`is_featured`),
  ADD KEY `idx_available` (`is_available`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`review_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `idx_product` (`product_id`),
  ADD KEY `idx_user` (`user_id`),
  ADD KEY `idx_status` (`status`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_email` (`email`),
  ADD KEY `idx_username` (`username`),
  ADD KEY `idx_role` (`role`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `addresses`
--
ALTER TABLE `addresses`
  MODIFY `address_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `carts`
--
ALTER TABLE `carts`
  MODIFY `cart_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `cart_items`
--
ALTER TABLE `cart_items`
  MODIFY `cart_item_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `favorites`
--
ALTER TABLE `favorites`
  MODIFY `favorite_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `notification_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=68;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `order_item_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=116;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `product_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `review_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `addresses`
--
ALTER TABLE `addresses`
  ADD CONSTRAINT `addresses_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `carts`
--
ALTER TABLE `carts`
  ADD CONSTRAINT `carts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `cart_items`
--
ALTER TABLE `cart_items`
  ADD CONSTRAINT `cart_items_ibfk_1` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`cart_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `cart_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE;

--
-- Constraints for table `favorites`
--
ALTER TABLE `favorites`
  ADD CONSTRAINT `favorites_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `favorites_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE;

--
-- Constraints for table `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE;

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE CASCADE;

--
-- Constraints for table `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `reviews_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `reviews_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `reviews_ibfk_3` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
