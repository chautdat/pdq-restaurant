# 🍽️ PDQ Restaurant - Food Ordering System

<p align="center">
  <img src="frontend/public/taco.png" alt="PDQ Restaurant Logo" width="120"/>
</p>

<p align="center">
  <strong>A modern full-stack restaurant food ordering platform</strong>
</p>

<p align="center">
  <a href="https://github.com/chautdat/pdq-restaurant/actions/workflows/ci.yml">
    <img src="https://github.com/chautdat/pdq-restaurant/actions/workflows/ci.yml/badge.svg" alt="CI - Build & Test"/>
  </a>
  <a href="https://github.com/chautdat/pdq-restaurant/actions/workflows/cd.yml">
    <img src="https://github.com/chautdat/pdq-restaurant/actions/workflows/cd.yml/badge.svg" alt="CD - Deploy"/>
  </a>
  <a href="https://github.com/chautdat/pdq-restaurant/actions/workflows/backup.yml">
    <img src="https://github.com/chautdat/pdq-restaurant/actions/workflows/backup.yml/badge.svg" alt="Database Backup"/>
  </a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat&logo=springboot&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/Vue.js-3.x-4FC08D?style=flat&logo=vuedotjs&logoColor=white" alt="Vue.js"/>
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat&logo=mysql&logoColor=white" alt="MySQL"/>
  <img src="https://img.shields.io/badge/Redis-7.x-DC382D?style=flat&logo=redis&logoColor=white" alt="Redis"/>
  <img src="https://img.shields.io/badge/RabbitMQ-3.12-FF6600?style=flat&logo=rabbitmq&logoColor=white" alt="RabbitMQ"/>
  <img src="https://img.shields.io/badge/Docker-Ready-2496ED?style=flat&logo=docker&logoColor=white" alt="Docker"/>
</p>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [System Architecture](#-system-architecture)
- [Quick Start](#-quick-start)
- [API Endpoints](#-api-endpoints)
- [Database Schema](#-database-schema)
- [Shipping Fee Calculation](#-shipping-fee-calculation)
- [Promotional System](#-promotional-system)
- [Message Queue System](#-message-queue-system)
- [Payment Integration](#-payment-integration)
- [Deployment](#-deployment)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🎯 Overview

**PDQ Restaurant** is a comprehensive food ordering platform designed for restaurants to manage their online presence, handle customer orders, and streamline operations. The system supports both customer-facing ordering and administrative management functionalities.

### ✨ Key Highlights

| Feature               | Description                                |
| --------------------- | ------------------------------------------ |
| 🛒 **Smart Ordering** | Shopping cart with geo-location support    |
| 💳 **Multi-Payment**  | VNPay & ZaloPay integration                |
| 🤖 **AI Chatbot**     | Claude AI & Groq powered support           |
| 📧 **Email Queue**    | RabbitMQ async email delivery              |
| 🔔 **Real-time**      | WebSocket order tracking                   |
| 🚚 **Smart Shipping** | Distance-based fee calculation (Haversine) |
| 🎟️ **Promo System**   | Flexible discount codes                    |
| 📊 **Analytics**      | Real-time admin dashboard                  |

---

## 🚀 Features

### 👤 Customer Features

| Category            | Features                                                               |
| ------------------- | ---------------------------------------------------------------------- |
| **Menu & Browsing** | Browse by category, search, filter by price/availability, quick view   |
| **Shopping Cart**   | Add/remove items, quantity update, cart persistence, special notes     |
| **Checkout**        | Multiple addresses, geo-location, distance-based shipping, promo codes |
| **Payment**         | Cash on Delivery, VNPay, ZaloPay                                       |
| **Order Tracking**  | Real-time status, order history, cancellation                          |
| **Account**         | Registration, JWT auth, profile management                             |
| **AI Support**      | 24/7 chatbot, menu recommendations, order assistance                   |

### 👨‍💼 Admin Features

| Category             | Features                                                  |
| -------------------- | --------------------------------------------------------- |
| **Dashboard**        | Real-time analytics, revenue tracking, live notifications |
| **Menu Management**  | Product CRUD, categories, images, pricing                 |
| **Order Management** | Status updates, filtering, date range, receipts           |
| **Promo Codes**      | Create/manage codes, usage tracking, validity periods     |
| **User Management**  | Role management, account status, activity tracking        |
| **Kitchen Display**  | Real-time orders, preparation queue, completion tracking  |
| **Reports**          | Sales reports, best-sellers, payment breakdown            |

---

## 🛠 Tech Stack

### Backend

| Technology        | Version | Purpose                            |
| ----------------- | ------- | ---------------------------------- |
| Spring Boot       | 3.x     | Main backend framework             |
| MySQL             | 8.0     | Primary database                   |
| Redis             | 7.x     | Caching and session management     |
| RabbitMQ          | 3.12    | Message queue for async processing |
| WebSocket (STOMP) | -       | Real-time communication            |
| JWT               | -       | Authentication and authorization   |

### Frontend

| Technology   | Version | Purpose            |
| ------------ | ------- | ------------------ |
| Vue.js       | 3.x     | Frontend framework |
| Axios        | -       | HTTP client        |
| Tailwind CSS | -       | Styling            |
| SweetAlert2  | -       | User notifications |
| Vuex         | -       | State management   |

### DevOps & Infrastructure

| Technology              | Purpose                                 |
| ----------------------- | --------------------------------------- |
| Docker & Docker Compose | Containerization                        |
| Nginx                   | Reverse proxy and static file serving   |
| GitHub Actions          | CI/CD pipeline                          |
| Ngrok                   | Webhook tunneling for payment callbacks |

### AI Services

| Service               | Purpose                      |
| --------------------- | ---------------------------- |
| Claude AI (Anthropic) | Primary chatbot intelligence |
| Groq AI               | Fast inference backup        |

---

## 🏗 System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         NGINX (Port 8080)                        │
│                    Reverse Proxy + Static Files                  │
└─────────────────────────────────────────────────────────────────┘
                                  │
                    ┌─────────────┴─────────────┐
                    │                           │
                    ▼                           ▼
┌─────────────────────────┐     ┌─────────────────────────────────┐
│   Vue.js Frontend       │     │   Spring Boot Backend (Port 3000)│
│   - Customer Portal     │     │   - REST API                     │
│   - Admin Dashboard     │     │   - WebSocket Server             │
│   - Real-time Updates   │     │   - Payment Processing           │
└─────────────────────────┘     └─────────────────────────────────┘
                                              │
                    ┌─────────────┬───────────┼───────────┬────────┐
                    │             │           │           │        │
                    ▼             ▼           ▼           ▼        ▼
              ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐
              │  MySQL   │ │  Redis   │ │ RabbitMQ │ │  VNPay   │ │ ZaloPay  │
              │ Database │ │  Cache   │ │  Queue   │ │ Gateway  │ │ Gateway  │
              └──────────┘ └──────────┘ └──────────┘ └──────────┘ └──────────┘
```

---

## 📦 Quick Start

### Prerequisites

- Docker & Docker Compose
- 4GB RAM minimum
- Ports: 8080, 3000, 3306, 6379, 5672, 15672

### Option 1: Auto Setup (Recommended)

```bash
# Clone repository
git clone https://github.com/chautdat/pdq-restaurant.git
cd pdq-restaurant

# Run setup wizard
./setup.sh

# Start Docker
cd docker
docker-compose up -d
```

### Option 2: Manual Setup

```bash
# Clone repository
git clone https://github.com/chautdat/pdq-restaurant.git
cd pdq-restaurant/docker

# Copy environment template
cp .env.example .env

# Edit .env file with your API keys
nano .env

# Start Docker
docker-compose up -d
```

### 🌐 Access URLs

| Service         | URL                    | Credentials      |
| --------------- | ---------------------- | ---------------- |
| Frontend        | http://localhost:8080  | -                |
| Backend API     | http://localhost:3000  | -                |
| RabbitMQ UI     | http://localhost:15672 | admin / admin123 |
| Ngrok Inspector | http://localhost:4040  | -                |

---

## 🔌 API Endpoints

### Authentication

| Method | Endpoint             | Description        |
| ------ | -------------------- | ------------------ |
| `POST` | `/api/auth/register` | User registration  |
| `POST` | `/api/auth/login`    | User login         |
| `POST` | `/api/auth/verify`   | Email verification |
| `POST` | `/api/auth/refresh`  | Refresh JWT token  |

### Products

| Method   | Endpoint             | Description            |
| -------- | -------------------- | ---------------------- |
| `GET`    | `/api/products`      | List all products      |
| `GET`    | `/api/products/{id}` | Get product details    |
| `POST`   | `/api/products`      | Create product (Admin) |
| `PUT`    | `/api/products/{id}` | Update product (Admin) |
| `DELETE` | `/api/products/{id}` | Delete product (Admin) |

### Cart

| Method   | Endpoint               | Description      |
| -------- | ---------------------- | ---------------- |
| `GET`    | `/api/cart`            | Get user's cart  |
| `POST`   | `/api/cart/items`      | Add item to cart |
| `PUT`    | `/api/cart/items/{id}` | Update cart item |
| `DELETE` | `/api/cart/items/{id}` | Remove cart item |
| `DELETE` | `/api/cart`            | Clear cart       |

### Orders

| Method | Endpoint                  | Description           |
| ------ | ------------------------- | --------------------- |
| `GET`  | `/api/orders`             | List user's orders    |
| `GET`  | `/api/orders/{id}`        | Get order details     |
| `POST` | `/api/orders`             | Create new order      |
| `PUT`  | `/api/orders/{id}/status` | Update status (Admin) |
| `POST` | `/api/orders/{id}/cancel` | Cancel order          |

### Payments

| Method | Endpoint                        | Description            |
| ------ | ------------------------------- | ---------------------- |
| `POST` | `/api/payment/vnpay/create`     | Create VNPay payment   |
| `GET`  | `/api/payment/vnpay/callback`   | VNPay callback         |
| `POST` | `/api/payment/zalopay/create`   | Create ZaloPay payment |
| `POST` | `/api/payment/zalopay/callback` | ZaloPay callback       |

### Promo Codes

| Method   | Endpoint                           | Description            |
| -------- | ---------------------------------- | ---------------------- |
| `GET`    | `/api/promo-codes/validate/{code}` | Validate promo code    |
| `GET`    | `/api/promo-codes`                 | List all codes (Admin) |
| `POST`   | `/api/promo-codes`                 | Create code (Admin)    |
| `PUT`    | `/api/promo-codes/{id}`            | Update code (Admin)    |
| `DELETE` | `/api/promo-codes/{id}`            | Delete code (Admin)    |
| `PUT`    | `/api/promo-codes/{id}/toggle`     | Toggle active status   |

---

## 🗄 Database Schema

### Core Tables

| Table         | Description                             |
| ------------- | --------------------------------------- |
| `users`       | Customer and admin accounts             |
| `products`    | Menu items with prices and descriptions |
| `categories`  | Product categorization                  |
| `carts`       | Shopping cart storage                   |
| `cart_items`  | Individual cart items                   |
| `orders`      | Order records                           |
| `order_items` | Individual order line items             |
| `promo_codes` | Promotional discount codes              |
| `payments`    | Payment transaction records             |
| `addresses`   | User delivery addresses                 |
| `reviews`     | Product and order reviews               |

### Promo Codes Schema

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

---

## 🚚 Shipping Fee Calculation

### Haversine Formula

Distance-based pricing using GPS coordinates to calculate the distance between the restaurant and delivery address.

**Formula:**

```
a = sin²(Δlat/2) + cos(lat1) × cos(lat2) × sin²(Δlng/2)
c = 2 × atan2(√a, √(1−a))
distance = R × c
```

Where:

- `lat1, lng1`: Restaurant coordinates
- `lat2, lng2`: Delivery coordinates
- `R`: Earth's radius = 6,371 km

### Pricing Table

| Distance  | Calculation                     | Fee        |
| --------- | ------------------------------- | ---------- |
| 0 - 3 km  | Base rate                       | 15,000 VND |
| 3 - 4 km  | 15,000 + (distance - 3) × 5,000 | 20,000 VND |
| 4 - 5 km  | 15,000 + (distance - 3) × 5,000 | 25,000 VND |
| 5 - 10 km | 15,000 + (distance - 3) × 5,000 | 50,000 VND |
| 10+ km    | 15,000 + (distance - 3) × 5,000 | Dynamic    |

### Implementation (Java)

```java
private double haversineDistanceKm(double lat1, double lng1, double lat2, double lng2) {
    double R = 6371; // Earth's radius (km)

    double latDistance = Math.toRadians(lat2 - lat1);
    double lngDistance = Math.toRadians(lng2 - lng1);

    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
               Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return R * c;
}

public int calculateShippingFee(double distanceKm, boolean hasFreeShippingPromo) {
    if (hasFreeShippingPromo) {
        return 0;
    }

    if (distanceKm <= 0) {
        distanceKm = 1.0;
    }

    if (distanceKm <= 3) {
        return 15000;
    } else {
        int additionalFee = (int) Math.round((distanceKm - 3) * 5000);
        return 15000 + additionalFee;
    }
}
```

### Example Calculation

```
📍 Delivery Address: District 7, HCMC (lat=10.7234, lng=106.7346)
📍 Restaurant: District 1, HCMC (lat=10.7769, lng=106.7009)

Step 1: Calculate Haversine distance = 6.2 km

Step 2: Calculate shipping fee
   Since 6.2 km > 3 km:
   Fee = 15,000 + (6.2 - 3) × 5,000
       = 15,000 + 16,000
       = 31,000 VND ✓
```

---

## 🎟️ Promotional System

### Discount Types

| Type            | Description                 | Example                  |
| --------------- | --------------------------- | ------------------------ |
| `PERCENTAGE`    | Percentage off with max cap | 25% off, max 500,000 VND |
| `FIXED_AMOUNT`  | Fixed amount discount       | 50,000 VND off           |
| `FREE_SHIPPING` | Free delivery               | 0 VND shipping           |

### Promo Code Example

```json
{
  "code": "WELCOME25",
  "type": "PERCENTAGE",
  "value": 25,
  "maxDiscountAmount": 500000,
  "minAmount": 200000,
  "usageLimit": 100,
  "usedCount": 42,
  "active": true,
  "startDate": "2024-01-01",
  "endDate": "2024-12-31"
}
```

### Validation Flow

```
START
  │
  ├─► 1. Check code exists in DB
  │     ❌ No → Error: "Invalid promo code"
  │     ✓ Yes → Continue
  │
  ├─► 2. Check status = ACTIVE
  │     ❌ Inactive → Error: "Promo code is inactive"
  │     ✓ Active → Continue
  │
  ├─► 3. Check expiration date
  │     ❌ Expired → Error: "Promo code expired"
  │     ✓ Valid → Continue
  │
  ├─► 4. Check usage limit
  │     ❌ Exceeded → Error: "Usage limit exceeded"
  │     ✓ Available → Continue
  │
  ├─► 5. Check minimum order amount (minAmount)
  │     ❌ Not enough → Error: "Minimum order amount required"
  │     ✓ Sufficient → Continue
  │
  └─► ✅ VALID - Apply discount
      Update: usedCount++
END
```

### Calculation Examples

#### PERCENTAGE Discount

```
📦 ORDER:
   2× Chicken Rice @ 85,000 VND
   1× Orange Juice @ 15,000 VND
   SUBTOTAL: 185,000 VND

💳 PROMO: "WELCOME25" (25%, max 500,000 VND)
   Discount = 185,000 × 25% = 46,250 VND

🚚 Shipping: 4.5 km → 22,500 VND

💰 FINAL: 185,000 - 46,250 + 22,500 = 161,250 VND
```

#### FREE_SHIPPING Discount

```
📦 ORDER:
   3× Pho Bo @ 65,000 VND
   SUBTOTAL: 195,000 VND

💳 PROMO: "SHIPPING2024" (FREE_SHIPPING)
   Product Discount = 0 VND

🚚 Shipping: 8.2 km → Normal: 41,000 VND
   ✅ With promo: 0 VND (Save 41,000 VND!)

💰 FINAL: 195,000 - 0 + 0 = 195,000 VND
```

### Auto-generated Signup Code

When a user registers, they automatically receive a **SIGNUP\_[userId]** code with 20% discount.

---

## 📨 Message Queue System

### RabbitMQ Configuration

| Setting         | Value                        |
| --------------- | ---------------------------- |
| Host            | localhost (Docker: rabbitmq) |
| AMQP Port       | 5672                         |
| Management Port | 15672                        |
| Username        | admin                        |
| Password        | admin123                     |

### Implemented Queues

#### 📧 Email Queue (`email.queue`)

Handles asynchronous email delivery:

- Order confirmation emails
- Payment notifications
- Account verification
- Password reset emails
- Promotional campaigns

**Benefits:**

- Non-blocking email sending
- Automatic retry on failure
- Scalable processing

#### 📦 Order Queue (`order.queue`)

Background order processing:

- Status updates
- Inventory management
- Kitchen notifications
- Delivery assignment

#### 🔔 Notification Queue (`notification.queue`)

Real-time alerts:

- WebSocket broadcasts
- Push notifications
- Admin alerts

### Message Flow

```
1. Event Triggered → Backend publishes to queue
2. RabbitMQ stores message reliably
3. Consumer picks up message
4. Service processes message
5. Success → Message acknowledged
6. Failure → Message requeued for retry
```

---

## 💳 Payment Integration

### Supported Gateways

| Gateway     | Features                                            |
| ----------- | --------------------------------------------------- |
| **VNPay**   | QR code, bank transfer, e-wallet, auto-verification |
| **ZaloPay** | E-wallet, QR code, real-time status                 |
| **COD**     | Cash on delivery                                    |

### Payment Flow

```
1. Customer selects payment method
2. System generates payment URL/QR
3. Customer completes payment
4. Gateway sends callback
5. Backend verifies & updates order
6. Customer receives confirmation
```

---

## 🐳 Deployment

### Docker Services (7 Containers)

| Service      | Port        | Description     |
| ------------ | ----------- | --------------- |
| pdq-frontend | 8080        | Vue.js + Nginx  |
| pdq-backend  | 3000        | Spring Boot API |
| pdq-mysql    | 3306        | MySQL 8.0       |
| pdq-redis    | 6379        | Redis cache     |
| pdq-rabbitmq | 5672, 15672 | Message broker  |
| pdq-ngrok    | 4040        | Webhook tunnel  |

### Environment Variables

| Variable              | Description         |
| --------------------- | ------------------- |
| `MYSQL_ROOT_PASSWORD` | Database password   |
| `JWT_SECRET`          | JWT signing key     |
| `VNPAY_TMN_CODE`      | VNPay merchant code |
| `VNPAY_HASH_SECRET`   | VNPay secret        |
| `ZALOPAY_APP_ID`      | ZaloPay app ID      |
| `CLAUDE_API_KEY`      | Anthropic API key   |
| `GROQ_API_KEY`        | Groq API key        |
| `RABBITMQ_USERNAME`   | RabbitMQ user       |
| `RABBITMQ_PASSWORD`   | RabbitMQ password   |

### Docker Commands

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Rebuild and start
docker-compose up -d --build

# Stop all services
docker-compose down

# Full cleanup
docker-compose down -v
docker rmi docker-backend docker-frontend
```

### Health Check

```bash
# Backend health
curl http://localhost:3000/actuator/health

# Frontend
curl http://localhost:8080
```

---

## 📁 Project Structure

```
pdq-restaurant/
├── pdq-BE/                          # Spring Boot Backend
│   ├── src/main/java/com/pdq/
│   │   ├── controller/
│   │   │   ├── PromoCodeController.java
│   │   │   ├── OrderController.java
│   │   │   └── AuthController.java
│   │   ├── service/
│   │   │   ├── PromoCodeService.java
│   │   │   ├── OrderService.java
│   │   │   └── AuthService.java
│   │   ├── entity/
│   │   │   ├── PromoCode.java
│   │   │   └── Order.java
│   │   └── repository/
│   ├── src/main/resources/db/migration/
│   │   ├── V8__Create_promo_codes_table.sql
│   │   └── V9__Add_promo_code_to_orders.sql
│   └── pom.xml
│
├── frontend/                        # Vue.js Frontend
│   ├── src/
│   │   ├── admin/
│   │   │   ├── Orders.vue
│   │   │   ├── PromoCodeManager.vue
│   │   │   └── Dashboard.vue
│   │   ├── pages/
│   │   │   ├── Checkout.vue
│   │   │   ├── Menu.vue
│   │   │   └── Profile.vue
│   │   └── components/
│   └── package.json
│
├── docker/                          # Docker Deployment
│   ├── docker-compose.yml
│   ├── .env.example
│   ├── Dockerfile (backend)
│   ├── Dockerfile (frontend)
│   └── nginx.conf
│
└── README.md
```

---

## 🔒 Security

- ✅ JWT authentication
- ✅ API keys in `.env` (not committed)
- ✅ CORS configuration
- ✅ Input validation
- ✅ SQL injection prevention
- ✅ Rate limiting on promo codes
- ✅ Coordinate validation

---

## 🧪 Testing

### Backend Tests

```bash
cd pdq-BE
mvn test
```

### Frontend Tests

```bash
cd frontend
npm run test
```

### Integration Tests

```bash
cd docker
docker-compose up -d
curl http://localhost:3000/actuator/health
```

---

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📧 Contact

**Author:** Châu Tấn Đạt  
**GitHub:** [@chautdat](https://github.com/chautdat)

---

## 📝 License

This project is developed for educational purposes as part of a university capstone project.

---

<p align="center">
  ⭐ <strong>Star this repo if you find it helpful!</strong> ⭐
</p>
