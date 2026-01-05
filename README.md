# 🍽️ PDQ Restaurant - Food Ordering System# ��️ PDQ Restaurant - Food Ordering System

A full-stack web application for restaurant food ordering with modern features including online payment integration, real-time order tracking, AI chatbot support, and comprehensive admin management.[![CI - Build & Test](https://github.com/chautdat/pdq-restaurant/actions/workflows/ci.yml/badge.svg)](https://github.com/chautdat/pdq-restaurant/actions/workflows/ci.yml)

[![CD - Deploy](https://github.com/chautdat/pdq-restaurant/actions/workflows/cd.yml/badge.svg)](https://github.com/chautdat/pdq-restaurant/actions/workflows/cd.yml)

---[![Database Backup](https://github.com/chautdat/pdq-restaurant/actions/workflows/backup.yml/badge.svg)](https://github.com/chautdat/pdq-restaurant/actions/workflows/backup.yml)

[![Test CI](https://github.com/chautdat/pdq-restaurant/actions/workflows/test-simple.yml/badge.svg)](https://github.com/chautdat/pdq-restaurant/actions/workflows/test-simple.yml)

## 📋 Table of Contents

## 🚀 Features

- [Project Overview](#-project-overview)

- [Technology Stack](#-technology-stack)- 🛒 **Shopping Cart & Checkout** with geo-location support

- [System Architecture](#-system-architecture)- 💳 **Payment Integration** (VNPay, ZaloPay)

- [Features](#-features)- 🎟️ **Promotional Code System** (PERCENTAGE, FIXED_AMOUNT, FREE_SHIPPING)

  - [Customer Features](#customer-features)- 🚚 **Distance-based Shipping Fee Calculation** (Haversine formula)

  - [Admin Features](#admin-features)- 🤖 **AI Chatbot Support** (Claude AI + Groq)

  - [Payment Integration](#payment-integration)- 📧 **Email Queue System** (RabbitMQ)

  - [Real-time Features](#real-time-features)- 🔔 **Real-time Order Tracking** (WebSocket)

  - [AI Integration](#ai-integration)- 📊 **Admin Dashboard** with real-time analytics

- [Database Schema](#-database-schema) - 📈 Order management with advanced filtering

- [API Endpoints](#-api-endpoints) - 🎟️ Promo code management interface

- [Message Queue System](#-message-queue-system-rabbitmq) - 👥 User & staff management

- [Deployment](#-deployment)- 🐳 **Docker Deployment**

- 🚀 **CI/CD Pipeline** with GitHub Actions

---

## 🏗️ Tech Stack

## 🎯 Project Overview

### Backend

**PDQ Restaurant** is a comprehensive food ordering platform designed for restaurants to manage their online presence, handle customer orders, and streamline operations. The system supports both customer-facing ordering and administrative management functionalities.

- ☕ **Spring Boot 3.x**

### Key Highlights- 🗄️ **MySQL 8.0**

- 🔴 **Redis** (Caching)

- **Multi-payment Gateway Support**: VNPay and ZaloPay integration- 🐰 **RabbitMQ** (Message Queue)

- **Real-time Order Tracking**: WebSocket-based live updates- 🔌 **WebSocket** (STOMP)

- **AI-Powered Chatbot**: Intelligent customer support using Claude AI and Groq- 🔐 **JWT Authentication**

- **Message Queue System**: RabbitMQ for reliable email delivery and background processing- 📧 **Email Service**

- **Geo-location Services**: Distance-based shipping fee calculation

- **Promotional System**: Flexible discount codes with multiple types### Frontend

---- 🎨 **Vue.js 3**

- 🎯 **Axios**

## 🛠 Technology Stack- 💅 **Tailwind CSS**

- 🍬 **SweetAlert2**

### Backend

| Technology | Version | Purpose |### DevOps

|------------|---------|---------|

| Spring Boot | 3.x | Main backend framework |- 🐳 **Docker & Docker Compose**

| MySQL | 8.0 | Primary database |- 🚀 **GitHub Actions CI/CD**

| Redis | 7.x | Caching and session management |- 🌐 **Nginx**

| RabbitMQ | 3.12 | Message queue for async processing |

| WebSocket (STOMP) | - | Real-time communication |### AI Integration

| JWT | - | Authentication and authorization |

- �� **Claude AI** (Anthropic API)

### Frontend- 🧠 **Groq AI** (Fast inference)

| Technology | Version | Purpose |- 🔍 **Real-time Menu Search**

|------------|---------|---------|

| Vue.js | 3.x | Frontend framework |## 📦 Quick Start

| Axios | - | HTTP client |

| Tailwind CSS | - | Styling |### Option 1: Auto Setup (Recommended)

| SweetAlert2 | - | User notifications |

| Vuex | - | State management |```bash

# Clone repository

### DevOps & Infrastructuregit clone https://github.com/chautdat/pdq-restaurant.git

| Technology | Purpose |cd pdq-restaurant

|------------|---------|

| Docker & Docker Compose | Containerization |# Run setup wizard

| Nginx | Reverse proxy and static file serving |./setup.sh

| GitHub Actions | CI/CD pipeline |

| Ngrok | Webhook tunneling for payment callbacks |# Start Docker

cd docker

### AI Servicesdocker-compose up -d

| Service | Purpose |```

|---------|---------|

| Claude AI (Anthropic) | Primary chatbot intelligence |### Option 2: Manual Setup

| Groq AI | Fast inference backup |

````bash

---# Clone repository

git clone https://github.com/chautdat/pdq-restaurant.git

## 🏗 System Architecturecd pdq-restaurant/docker



```# Copy environment template

┌─────────────────────────────────────────────────────────────────┐cp .env.example .env

│                         NGINX (Port 8080)                        │

│                    Reverse Proxy + Static Files                  │# Edit .env file with your API keys

└─────────────────────────────────────────────────────────────────┘nano .env

                                  │

                    ┌─────────────┴─────────────┐# Start Docker

                    │                           │docker-compose up -d

                    ▼                           ▼```

┌─────────────────────────┐     ┌─────────────────────────────────┐

│   Vue.js Frontend       │     │   Spring Boot Backend (Port 3000)│## 🌐 Access URLs

│   - Customer Portal     │     │   - REST API                     │

│   - Admin Dashboard     │     │   - WebSocket Server             │After starting Docker:

│   - Real-time Updates   │     │   - Payment Processing           │

└─────────────────────────┘     └─────────────────────────────────┘- 🌐 **Frontend:** http://localhost:8080

                                              │- ⚡ **Backend API:** http://localhost:3000

                    ┌─────────────┬───────────┼───────────┬────────┐- 🐰 **RabbitMQ Management:** http://localhost:15672

                    │             │           │           │        │  - Username: `admin`

                    ▼             ▼           ▼           ▼        ▼  - Password: `admin123`

              ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐

              │  MySQL   │ │  Redis   │ │ RabbitMQ │ │  VNPay   │ │ ZaloPay  │## 🎯 CI/CD Pipeline

              │ Database │ │  Cache   │ │  Queue   │ │ Gateway  │ │ Gateway  │

              └──────────┘ └──────────┘ └──────────┘ └──────────┘ └──────────┘Automated pipeline with GitHub Actions:

````

- ✅ **CI**: Build & Test on every push

---- ✅ **CD**: Auto deploy to production

- ✅ **Backup**: Daily database backups

## ✨ Features- ✅ **Quality**: Code quality checks

### Customer Features## 🎟️ Promotional Code System

#### 🏠 Homepage### Features

- Featured products showcase

- Promotional banners- **3 Discount Types:**

- Quick navigation to menu categories - � **PERCENTAGE**: Percentage discount with max cap

- Restaurant information display - 💵 **FIXED_AMOUNT**: Fixed amount discount

  - 🚚 **FREE_SHIPPING**: Free shipping cost

#### 🍔 Menu & Product Browsing

- Browse products by category (Rice, Noodles, Drinks, Desserts, etc.)### Admin Management

- Product search functionality

- Filter by price range```vue

- Filter by availability status/admin/promo-codes → PromoCodeManager.vue - Create/Edit/Delete promo codes -

- Product quick view with detailed informationToggle active/inactive status - View usage statistics - Set validity date ranges

- Product images and descriptions- Configure usage limits

````

#### 🛒 Shopping Cart

- Add/remove items from cart### Promo Code Example

- Update item quantities

- Real-time price calculation```json

- Cart persistence across sessions{

- Item notes for special requests  "code": "WELCOME25",

  "type": "PERCENTAGE",

#### 📝 Checkout Process  "value": 25,

- Multiple delivery address support  "maxDiscountAmount": 500000,

- Address auto-complete with geo-location  "minAmount": 200000,

- Distance-based shipping fee calculation (Haversine formula)  "usageLimit": 100,

- Promo code application  "usedCount": 42,

- Multiple payment method selection  "active": true,

- Order summary review  "startDate": "2024-01-01",

  "endDate": "2024-12-31"

#### 💳 Payment Options}

- **Cash on Delivery (COD)**: Pay when order arrives```

- **VNPay**: Vietnamese e-wallet and bank transfer

- **ZaloPay**: Popular Vietnamese payment gateway### Auto-generated Signup Code



#### 📦 Order ManagementWhen a user registers, they automatically receive a **SIGNUP\_[userId]** code with 20% discount.

- View order history

- Real-time order status tracking---

- Order cancellation (within time limit)

- Payment retry for failed transactions## 🚚 Shipping Fee Calculation

- Order details with itemized breakdown

### Distance-Based Pricing (Haversine Formula)

#### 👤 User Account

- User registration with email verification```

- Secure login with JWT authenticationBase: 15,000 VND (0-3 km)

- Profile managementExtended: 5,000 VND per km (> 3 km)

- Password change functionality

- Order history accessExamples:

- 2 km → 15,000 VND

#### 🎟️ Promotional System- 3 km → 15,000 VND

- Apply discount codes at checkout- 5 km → 25,000 VND (15,000 + 2×5,000)

- Multiple promo types supported:- 10 km → 50,000 VND (15,000 + 7×5,000)

  - **PERCENTAGE**: Percentage discount (e.g., 10% off)```

  - **FIXED_AMOUNT**: Fixed amount discount (e.g., 50,000 VND off)

  - **FREE_SHIPPING**: Free delivery fee### Coordinates Required

- Minimum order value requirements

- Usage limits per codeFrontend must send:

- Expiration date validation

```json

#### 🤖 AI Chatbot Support{

- 24/7 intelligent customer support  "deliveryLat": 10.7769,

- Menu recommendations  "deliveryLng": 106.7009,

- Order assistance  ...

- FAQ responses}

- Natural language understanding```



#### 🪑 Table ReservationBackend validates and calculates distance from restaurant coordinates using **Haversine formula**.

- View available tables

- Reserve tables for dine-in### FREE_SHIPPING Override

- Table capacity information

If promo code is `FREE_SHIPPING` type, shipping fee is set to **0 VND**.

---

---

### Admin Features

## � **Chi Tiết Thuật Toán Tính Toán**

#### 📊 Dashboard

- Real-time analytics and statistics### 1️⃣ **Thuật Toán Tính Phí Giao Hàng (Shipping Fee)**

- Order count by status

- Revenue tracking (daily/weekly/monthly)#### **Công Thức Haversine - Tính Khoảng Cách**

- Top-selling products

- Customer statistics```

- Live order notificationsCông thức:

a = sin²(Δlat/2) + cos(lat1) × cos(lat2) × sin²(Δlng/2)

#### 🍽️ Menu Managementc = 2 × atan2(√a, √(1−a))

- Add new products with imagesdistance = R × c

- Edit product information

- Set product availabilityTrong đó:

- Manage product categories- lat1, lng1: Vĩ độ, kinh độ nhà hàng (Restaurant coordinates)

- Upload product images- lat2, lng2: Vĩ độ, kinh độ giao hàng (Delivery coordinates)

- Set prices and descriptions- R: Bán kính Trái Đất = 6,371 km

- Δlat, Δlng: Chênh lệch vĩ độ, kinh độ

#### 📁 Category Management```

- Create product categories

- Edit category names and icons#### **Ví Dụ Tính Toán:**

- Organize menu structure

- Category display order**Nhà hàng:** lat=10.7769, lng=106.7009 (Quận 1, TP.HCM)



#### 📋 Order Management```javascript

- View all orders with filtering// Haversine Implementation (Backend - Java)

- Filter by status (Pending, Confirmed, Preparing, Delivering, Completed, Cancelled)private double haversineDistanceKm(double lat1, double lng1, double lat2, double lng2) {

- Filter by date range  double R = 6371; // Bán kính Trái Đất (km)

- Filter by payment status

- Update order status  double latDistance = Math.toRadians(lat2 - lat1);

- View order details  double lngDistance = Math.toRadians(lng2 - lng1);

- Print order receipts

  double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +

#### 🎟️ Promo Code Management             Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *

- Create new promotional codes             Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

- Set discount type and value

- Configure usage limits  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

- Set validity period

- Enable/disable codes  return R * c; // Khoảng cách theo km

- Track code usage statistics}

````

#### 👥 User Management

- View all registered users#### **Bảng Giá Phí Giao Hàng:**

- User role management (Customer, Staff, Admin)

- Account status management| Khoảng Cách | Công Thức | Phí Giao Hàng |

- User activity tracking| ------------- | ------------------------------- | -------------- |

| **0 - 3 km** | Phí cố định | **15,000 VND** |

#### 👨‍🍳 Kitchen Display System (KDS)| **3 - 4 km** | 15,000 + (distance - 3) × 5,000 | **20,000 VND** |

- Real-time incoming orders| **4 - 5 km** | 15,000 + (distance - 3) × 5,000 | **25,000 VND** |

- Order preparation queue| **5 - 10 km** | 15,000 + (distance - 3) × 5,000 | **50,000 VND** |

- Mark items as prepared| **10+ km** | 15,000 + (distance - 3) × 5,000 | Tính động |

- Order completion tracking

#### **Thuật Toán Chi Tiết:**

#### 💰 Sales & Revenue Reports

- Daily sales reports```java

- Revenue analyticspublic int calculateShippingFee(double distanceKm, boolean hasFreeShippingPromo) {

- Best-selling products report // 1. Kiểm tra FREE_SHIPPING promo

- Payment method breakdown if (hasFreeShippingPromo) {

  return 0; // Miễn phí vận chuyển

--- }

### Payment Integration // 2. Đảm bảo khoảng cách tối thiểu = 1 km (nếu Haversine trả về 0)

if (distanceKm <= 0) {

#### VNPay Integration distanceKm = 1.0;

- Secure payment processing }

- QR code payment support

- Bank transfer support // 3. Tính phí dựa trên khoảng cách

- Automatic payment verification if (distanceKm <= 3) {

- Payment callback handling return 15000; // Phí cố định 0-3 km

- Refund processing capability } else {

  // Phí cơ bản + phí bổ sung theo khoảng cách vượt quá 3 km

#### ZaloPay Integration int additionalFee = (int) Math.round((distanceKm - 3) \* 5000);

- E-wallet payment return 15000 + additionalFee;

- QR code generation }

- Real-time payment status}

- Automatic order confirmation

// Ước tính thời gian giao hàng

#### Payment Flowprivate int estimateDeliveryTime(double distanceKm) {

1. Customer selects payment method at checkout // Giả định tốc độ bình quân: 30 km/h

2. System generates payment URL/QR code return (int) Math.round((distanceKm / 30.0) \* 60); // Trả về phút

3. Customer completes payment on gateway}

4. Gateway sends callback to backend```

5. Backend verifies and updates order status

6. Customer receives confirmation#### **Ví Dụ Cụ Thể:**

---```

📍 Địa chỉ giao: Quận 7, TP.HCM (lat=10.7234, lng=106.7346)

### Real-time Features

Bước 1: Tính khoảng cách Haversine

#### WebSocket Implementation Nhà hàng (10.7769, 106.7009) → Địa chỉ (10.7234, 106.7346)

- **Order Status Updates**: Customers receive instant notifications when order status changes Khoảng cách = 6.2 km

- **Kitchen Notifications**: New orders appear instantly on kitchen display

- **Admin Dashboard**: Live statistics and order feedBước 2: Tính phí giao hàng

- **Chat Support**: Real-time messaging with AI chatbot Vì 6.2 km > 3 km:

  Phí = 15,000 + (6.2 - 3) × 5,000

#### Notification Types = 15,000 + 3.2 × 5,000

- Order confirmation = 15,000 + 16,000

- Order status changes = 31,000 VND ✓

- Payment success/failure

- Delivery updatesBước 3: Ước tính thời gian

  Thời gian = (6.2 / 30) × 60 = 12.4 phút ≈ 12 phút

---```

### AI Integration---

#### Claude AI (Anthropic)### 2️⃣ **Thuật Toán Tính Khuyến Mãi (Promotion Calculation)**

- Primary conversational AI

- Menu recommendations based on preferences#### **3 Loại Khuyến Mãi:**

- Order assistance and modifications

- Customer query handling```

- Natural language menu search┌─────────────────────────────────────────────────────────────┐

│ DISCOUNT TYPES │

#### Groq AI├──────────────────┬────────────────┬──────────────────────────┤

- Fast inference for quick responses│ PERCENTAGE │ FIXED_AMOUNT │ FREE_SHIPPING │

- Backup AI service├──────────────────┼────────────────┼──────────────────────────┤

- High-performance query processing│ Giảm theo % │ Giảm cố định │ Miễn phí vận chuyển │

│ Có giới hạn max │ Toàn bộ số tiền│ Áp dụng trực tiếp │

#### Chatbot Capabilities│ Ví dụ: 25% max │ Ví dụ: 50,000 │ shipping_fee = 0 │

- Menu exploration and recommendations│ 500,000 VND │ VND │ │

- Price inquiries└──────────────────┴────────────────┴──────────────────────────┘

- Order status checking```

- Restaurant information

- Operating hours#### **Quy Trình Tính Toán Khuyến Mãi:**

- Special dietary requirements

- Promotional information```javascript

/\*\*

--- \* Quy trình tính toán tổng tiền cuối cùng

-

## 🗄 Database Schema \* 1. Tính tổng phụ = Tổng giá trị sản phẩm

- 2.  Kiểm tra mã khuyến mãi có hợp lệ

### Core Tables \* 3. Tính khuyến mãi theo loại

- 4.  Tính phí giao hàng (có thể bị ghi đè bởi FREE_SHIPPING)

| Table | Description | \* 5. Tổng tiền cuối cùng = Tổng phụ - Khuyến mãi + Phí giao

|-------|-------------| \*/

| `users` | Customer and admin accounts |

| `products` | Menu items with prices and descriptions |function calculateFinalPrice(order) {

| `categories` | Product categorization | // Bước 1: Tính tổng phụ

| `carts` | Shopping cart storage | let subtotal = 0;

| `cart_items` | Individual cart items | order.items.forEach((item) => {

| `orders` | Order records | subtotal += item.price \* item.quantity;

| `order_items` | Individual order line items | });

| `promo_codes` | Promotional discount codes | console.log(`📊 Subtotal: ${subtotal.toLocaleString()} VND`);

| `payments` | Payment transaction records |

| `addresses` | User delivery addresses | // Bước 2: Kiểm tra mã khuyến mãi

| `reviews` | Product and order reviews | let discount = 0;

let discountType = null;

---

if (order.promoCode) {

## 🔌 API Endpoints const promo = validatePromoCode(order.promoCode, subtotal);

### Authentication if (promo.isValid) {

| Method | Endpoint | Description | discountType = promo.type;

|--------|----------|-------------|

| POST | `/api/auth/register` | User registration | // Bước 3: Tính khuyến mãi theo loại

| POST | `/api/auth/login` | User login | switch (promo.type) {

| POST | `/api/auth/verify` | Email verification | case "PERCENTAGE":

| POST | `/api/auth/refresh` | Refresh JWT token | // Tính % và giới hạn bằng maxDiscountAmount

          const percentageDiscount = subtotal * (promo.value / 100);

### Products discount = Math.min(percentageDiscount, promo.maxDiscountAmount);

| Method | Endpoint | Description | console.log(

|--------|----------|-------------| `💵 PERCENTAGE: ${promo.value}% = ${discount.toLocaleString()} VND`

| GET | `/api/products` | List all products | );

| GET | `/api/products/{id}` | Get product details | break;

| POST | `/api/products` | Create product (Admin) |

| PUT | `/api/products/{id}` | Update product (Admin) | case "FIXED_AMOUNT":

| DELETE | `/api/products/{id}` | Delete product (Admin) | // Giảm số tiền cố định (không vượt quá subtotal)

          discount = Math.min(promo.value, subtotal);

### Cart console.log(`💵 FIXED_AMOUNT: ${discount.toLocaleString()} VND`);

| Method | Endpoint | Description | break;

|--------|----------|-------------|

| GET | `/api/cart` | Get user's cart | case "FREE_SHIPPING":

| POST | `/api/cart/items` | Add item to cart | // Khuyến mãi này ghi đè phí giao - xử lý sau

| PUT | `/api/cart/items/{id}` | Update cart item | console.log(`🚚 FREE_SHIPPING: Phí giao = 0 VND`);

| DELETE | `/api/cart/items/{id}` | Remove cart item | break;

| DELETE | `/api/cart` | Clear cart | }

    }

### Orders }

| Method | Endpoint | Description |

|--------|----------|-------------| // Bước 4: Tính phí giao hàng

| GET | `/api/orders` | List user's orders | let shippingFee = calculateShippingFee(

| GET | `/api/orders/{id}` | Get order details | order.deliveryDistance,

| POST | `/api/orders` | Create new order | discountType === "FREE_SHIPPING" // Ghi đè nếu là FREE_SHIPPING

| PUT | `/api/orders/{id}/status` | Update order status | );

| POST | `/api/orders/{id}/cancel` | Cancel order | console.log(`🚚 Shipping Fee: ${shippingFee.toLocaleString()} VND`);

### Payments // Bước 5: Tính tổng tiền cuối cùng

| Method | Endpoint | Description | const finalTotal = subtotal - discount + shippingFee;

|--------|----------|-------------|

| POST | `/api/payment/vnpay/create` | Create VNPay payment | return {

| GET | `/api/payment/vnpay/callback` | VNPay callback handler | subtotal,

| POST | `/api/payment/zalopay/create` | Create ZaloPay payment | discount,

| POST | `/api/payment/zalopay/callback` | ZaloPay callback handler | shippingFee,

    finalTotal,

### Promo Codes breakdown: {

| Method | Endpoint | Description | subtotal: `${subtotal.toLocaleString()} VND`,

|--------|----------|-------------| discount: `-${discount.toLocaleString()} VND`,

| POST | `/api/promo/validate` | Validate promo code | shippingFee: `+${shippingFee.toLocaleString()} VND`,

| GET | `/api/admin/promo-codes` | List all codes (Admin) | finalTotal: `${finalTotal.toLocaleString()} VND`,

| POST | `/api/admin/promo-codes` | Create code (Admin) | },

};

---}

````

## 📨 Message Queue System (RabbitMQ)

#### **Ví Dụ Tính Toán Khuyến Mãi (PERCENTAGE):**

### Overview

RabbitMQ is used for asynchronous message processing, ensuring reliable delivery of time-sensitive operations without blocking the main application thread.```

📦 ĐƠN HÀNG EXAMPLE:

### Queue Configuration┌─────────────────────────────────────────┐

| Setting | Value |│ 2× Cơm Gà Hainaam @ 85,000 VND         │

|---------|-------|│ 1× Nước Cam @ 15,000 VND               │

| Host | localhost (Docker: rabbitmq) |│ SUBTOTAL:                    180,000 VND │

| AMQP Port | 5672 |└─────────────────────────────────────────┘

| Management Port | 15672 |

| Virtual Host | / |💳 PROMO CODE: "WELCOME25"

| Default Username | admin |   Type: PERCENTAGE

| Default Password | admin123 |   Value: 25%

   Max Discount: 500,000 VND

### Implemented Queues   Min Amount: 150,000 VND ✓ (180,000 ≥ 150,000)



#### 1. Email Queue (`email.queue`)🧮 TÍNH KHUYẾN MÃI:

**Purpose**: Asynchronous email delivery for order confirmations, notifications, and promotional emails.   Discount = 180,000 × (25 / 100) = 45,000 VND

   (45,000 < 500,000, nên áp dụng 45,000)

**Message Types**:

- Order confirmation emails🚚 PHƯƠNG TIỆN GIAO:

- Payment success/failure notifications   Distance = 4.5 km

- Account verification emails   Shipping = 15,000 + (4.5 - 3) × 5,000 = 22,500 VND

- Password reset emails

- Promotional campaign emails💰 TỔNG TIỀN CUỐI CÙNG:

   Final = 180,000 - 45,000 + 22,500 = 157,500 VND

**Benefits**:```

- Non-blocking email sending

- Automatic retry on failure#### **Ví Dụ Tính Toán Khuyến Mãi (FREE_SHIPPING):**

- Email delivery tracking

- Scalable email processing```

📦 ĐƠN HÀNG EXAMPLE:

#### 2. Order Processing Queue (`order.queue`)┌─────────────────────────────────────────┐

**Purpose**: Handle order-related background tasks.│ 3× Phở Bò Tái Nam @ 65,000 VND         │

│ 1× Nước Chanh @ 12,000 VND             │

**Operations**:│ SUBTOTAL:                    207,000 VND │

- Order status updates└─────────────────────────────────────────┘

- Inventory updates

- Kitchen notification dispatch💳 PROMO CODE: "SHIPPING2024"

- Delivery assignment   Type: FREE_SHIPPING

   (Tự động miễn phí vận chuyển)

#### 3. Notification Queue (`notification.queue`)

**Purpose**: Push notifications and real-time alerts.🧮 TÍNH KHUYẾN MÃI:

   Discount = 0 VND (FREE_SHIPPING không giảm giá sản phẩm)

**Message Types**:

- WebSocket broadcasts🚚 PHƯƠNG TIỆN GIAO:

- Mobile push notifications   Distance = 8.2 km

- Admin alerts   Normal Shipping = 15,000 + (8.2 - 3) × 5,000 = 41,000 VND

   ❌ NHƯNG có FREE_SHIPPING promo

### Message Flow Example (Email)   ✅ Actual Shipping = 0 VND (Giảm 41,000 VND)

````

1. Order Created → Backend publishes to email.queue💰 TỔNG TIỀN CUỐI CÙNG:

2. RabbitMQ stores message reliably Final = 207,000 - 0 + 0 = 207,000 VND

3. Email Consumer picks up message (Tiết kiệm 41,000 VND phí vận chuyển!)

4. Email service sends email```

5. On success: Message acknowledged

6. On failure: Message requeued for retry#### **Ví Dụ Tính Toán Khuyến Mãi (FIXED_AMOUNT):**

```

```

### RabbitMQ Management Console📦 ĐƠN HÀNG EXAMPLE:

Access the management interface to monitor queues, messages, and consumers.┌─────────────────────────────────────────┐

│ 2× Cơm Chiên Đương Châu @ 75,000 VND │

- **URL**: http://localhost:15672│ 1× Trà Sữa @ 35,000 VND │

- **Username**: admin│ SUBTOTAL: 185,000 VND │

- **Password**: admin123└─────────────────────────────────────────┘

**Features Available**:💳 PROMO CODE: "FIXED50K"

- Queue monitoring and statistics Type: FIXED_AMOUNT

- Message inspection and publishing Value: 50,000 VND (cố định)

- Consumer management Min Amount: 150,000 VND ✓ (185,000 ≥ 150,000)

- Performance metrics

- Dead letter queue handling🧮 TÍNH KHUYẾN MÃI:

- Exchange and binding configuration Discount = min(50,000, 185,000) = 50,000 VND

  (50,000 < 185,000, nên áp dụng toàn bộ)

---

🚚 PHƯƠNG TIỆN GIAO:

## 🚀 Deployment Distance = 2.8 km

Shipping = 15,000 VND (0-3 km)

### Prerequisites

- Docker and Docker Compose installed💰 TỔNG TIỀN CUỐI CÙNG:

- Minimum 4GB RAM recommended Final = 185,000 - 50,000 + 15,000 = 150,000 VND

- Available ports: 8080, 3000, 3306, 6379, 5672, 15672, 4040```

### Services Overview---

| Service | Port | Description |

|---------|------|-------------|### 3️⃣ **Quy Trình Xác Thực Mã Khuyến Mãi**

| Frontend | 8080 | Vue.js application served by Nginx |

| Backend | 3000 | Spring Boot REST API |```

| MySQL | 3306 | Primary database |START

| Redis | 6379 | Caching layer | │

| RabbitMQ | 5672, 15672 | Message queue (AMQP + Management UI) | ├─► 1. Kiểm tra code tồn tại trong DB

| Ngrok | 4040 | Payment webhook tunnel | │ ❌ Không → Error: "Invalid promo code"

│ ✓ Có → Tiếp tục

### Environment Variables │

| Variable | Description | ├─► 2. Kiểm tra status = ACTIVE

|----------|-------------| │ ❌ Inactive → Error: "Promo code is inactive"

| `MYSQL_ROOT_PASSWORD` | Database root password | │ ✓ Active → Tiếp tục

| `JWT_SECRET` | JWT signing key | │

| `VNPAY_TMN_CODE` | VNPay merchant code | ├─► 3. Kiểm tra ngày hết hạn

| `VNPAY_HASH_SECRET` | VNPay secret key | │ ❌ Hết hạn → Error: "Promo code expired"

| `ZALOPAY_APP_ID` | ZaloPay application ID | │ ✓ Còn hạn → Tiếp tục

| `ZALOPAY_KEY1` | ZaloPay key 1 | │

| `ZALOPAY_KEY2` | ZaloPay key 2 | ├─► 4. Kiểm tra giới hạn sử dụng

| `CLAUDE_API_KEY` | Anthropic Claude API key | │ ❌ Vượt quá → Error: "Usage limit exceeded"

| `GROQ_API_KEY` | Groq API key | │ ✓ Còn quota → Tiếp tục

| `RABBITMQ_HOST` | RabbitMQ server host | │

| `RABBITMQ_USERNAME` | RabbitMQ username | ├─► 5. Kiểm tra tối thiểu đơn hàng (minAmount)

| `RABBITMQ_PASSWORD` | RabbitMQ password | │ ❌ Không đủ → Error: "Minimum order amount required"

| `MAIL_USERNAME` | SMTP email username | │ ✓ Đủ → Tiếp tục

| `MAIL_PASSWORD` | SMTP email password | │

└─► ✅ VALID - Áp dụng khuyến mãi

### Access URLs (After Deployment) Cập nhật: usedCount++

| Service | URL |END

|---------|-----|```

| Frontend | http://localhost:8080 |

| Backend API | http://localhost:3000 |---

| RabbitMQ Management | http://localhost:15672 |

| Ngrok Inspector | http://localhost:4040 |```

pdq-restaurant/

---├── pdq-BE/ # Spring Boot Backend

│ ├── src/main/java/com/pdq/

## 📄 License│ │ ├── controller/

│ │ │ ├── PromoCodeController.java # 7 REST endpoints

This project is developed for educational purposes as part of a university capstone project.│ │ │ ├── OrderController.java

│ │ │ └── AuthController.java

---│ │ ├── service/

│ │ │ ├── PromoCodeService.java # 15+ methods for CRUD & validation

## 👥 Author│ │ │ ├── OrderService.java # Haversine shipping calculation

│ │ │ └── AuthService.java # Auto promo code generation

**Chau Tan Dat** - Full Stack Developer│ │ ├── entity/

│ │ │ ├── PromoCode.java # 18 fields, DiscountType enum

---│ │ │ └── Order.java # promo_code & lat/lng fields

│ │ └── repository/PromoCodeRepository.java

## 🙏 Acknowledgments│ ├── src/main/resources/db/migration/

│ │ ├── V8\_\_Create_promo_codes_table.sql

- Spring Boot Documentation│ │ └── V9\_\_Add_promo_code_to_orders.sql

- Vue.js Documentation│ └── pom.xml

- VNPay Integration Guide│

- ZaloPay Developer Documentation├── frontend/ # Vue.js Frontend

- RabbitMQ Documentation│ ├── src/

- Anthropic Claude API Documentation│ │ ├── admin/

- Groq AI Platform Documentation│ │ │ ├── Orders.vue # Order management, filters, Material Design buttons

│ │ │ ├── PromoCodeManager.vue # Create/Edit/Delete promo codes
│ │ │ └── Dashboard.vue
│ │ ├── pages/
│ │ │ ├── Checkout.vue # Geo-location, promo code input
│ │ │ ├── Menu.vue
│ │ │ └── Profile.vue
│ │ └── components/
│ └── package.json
│
├── docker/ # Docker Deployment
│ ├── docker-compose.yml # 7 services: Backend, Frontend, MySQL, Redis, RabbitMQ, Ngrok
│ ├── .env.example
│ ├── Dockerfile (backend)
│ ├── Dockerfile (frontend)
│ └── nginx.conf
│
└── README.md

```

---

## 🔌 REST API Endpoints

### Promo Code Endpoints (PromoCodeController)

```

POST /api/promo-codes # Create new promo code (Admin)
GET /api/promo-codes # Get all promo codes (Admin)
GET /api/promo-codes/:id # Get details
PUT /api/promo-codes/:id # Update promo code (Admin)
DELETE /api/promo-codes/:id # Delete promo code (Admin)
PUT /api/promo-codes/:id/toggle # Toggle active status
GET /api/promo-codes/validate/:code # Validate code for checkout

```

### Order Integration

```

POST /api/orders # Create order with promo code validation
{
"items": [...],
"promoCode": "WELCOME25", # Optional
"deliveryLat": 10.7769,
"deliveryLng": 106.7009,
...
}

````

---

## 🧪 Testing

### Backend Tests

```bash
cd pdq-BE
mvn test
````

### Frontend Tests

```bash
cd frontend
npm run test
```

### Integration Tests

```bash
cd docker
docker-compose up -d
# Wait for services to start
curl http://localhost:3000/actuator/health
```

## 🔒 Security

- ✅ API keys stored in `.env` files (not committed to Git)
- ✅ JWT authentication for API endpoints
- ✅ CORS configuration
- ✅ Input validation (promo code, coordinates, order amounts)
- ✅ SQL injection prevention
- ✅ Lat/Lng coordinate validation before order creation
- ✅ Rate limiting on promo code usage

---

## 💾 Database Schema

### promo_codes Table (Migration V8)

```sql
CREATE TABLE promo_codes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) UNIQUE NOT NULL,
  type ENUM('PERCENTAGE', 'FIXED_AMOUNT', 'FREE_SHIPPING') NOT NULL,
  value DECIMAL(10,2) NOT NULL,
  max_discount_amount BIGINT,
  min_amount BIGINT,
  usage_limit INT,
  used_count INT DEFAULT 0,
  active BOOLEAN DEFAULT true,
  created_by BIGINT,
  start_date DATE,
  end_date DATE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_code (code),
  INDEX idx_active (active)
);
```

### orders Table Updates (Migration V9)

```sql
ALTER TABLE orders ADD COLUMN promo_code VARCHAR(50);
ALTER TABLE orders ADD COLUMN delivery_lat DOUBLE;
ALTER TABLE orders ADD COLUMN delivery_lng DOUBLE;
ALTER TABLE orders ADD FOREIGN KEY (promo_code) REFERENCES promo_codes(code);
```

---

## � Docker Deployment

### Services (7 Containers)

1. **pdq-backend** - Spring Boot application (Java 17)
2. **pdq-frontend** - Vue.js application (Nginx)
3. **pdq-mysql** - MySQL 8.0 database
4. **pdq-redis** - Redis cache
5. **pdq-rabbitmq** - RabbitMQ message broker
6. **pdq-ngrok** - Ngrok tunnel for external access
7. **Database migrations** - Flyway (auto-run on startup)

### Quick Start

```bash
cd docker

# Option 1: Full cleanup and rebuild
docker-compose down -v
docker rmi docker-backend docker-frontend
docker-compose build --no-cache
docker-compose up -d

# Option 2: Quick restart (if images exist)
docker-compose up -d

# Option 3: Stop and clean
docker-compose down
```

### Environment Configuration

Copy `.env.example` to `.env`:

```bash
# Backend API Keys
GOOGLE_MAPS_API_KEY=your_key_here
CLAUDE_API_KEY=your_key_here

# Payment Integration
VNPAY_SECRET=your_secret_here
ZALOPAY_KEY=your_key_here

# Email Service
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USERNAME=your_email@gmail.com
SMTP_PASSWORD=your_app_password
```

### Health Check

```bash
# Backend health
curl http://localhost:3000/actuator/health

# Frontend
curl http://localhost:8080

# RabbitMQ Management UI
http://localhost:15672
Username: admin
Password: admin123
```

---

## 📊 Monitoring & Logs

### Docker Logs

```bash
# Backend logs
docker logs pdq-backend -f

# Frontend logs
docker logs pdq-frontend -f

# All services
docker-compose logs -f
```

### Database Logs

```bash
# MySQL logs
docker exec pdq-mysql tail -f /var/log/mysql/error.log
```

---

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## �📧 Contact

**Author:** Châu Tấn Đạt
**Email:** chautdat@example.com
**GitHub:** [@chautdat](https://github.com/chautdat)

## 📝 License

MIT License

---

⭐ **Star this repo if you find it helpful!**
