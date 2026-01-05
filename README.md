# 🍽️ PDQ Restaurant - Food Ordering System# 🍽️ PDQ Restaurant - Food Ordering System# ��️ PDQ Restaurant - Food Ordering System

<p align="center">A full-stack web application for restaurant food ordering with modern features including online payment integration, real-time order tracking, AI chatbot support, and comprehensive admin management.[![CI - Build & Test](https://github.com/chautdat/pdq-restaurant/actions/workflows/ci.yml/badge.svg)](https://github.com/chautdat/pdq-restaurant/actions/workflows/ci.yml)

  <img src="frontend/public/taco.png" alt="PDQ Restaurant Logo" width="120"/>

</p>[![CD - Deploy](https://github.com/chautdat/pdq-restaurant/actions/workflows/cd.yml/badge.svg)](https://github.com/chautdat/pdq-restaurant/actions/workflows/cd.yml)

<p align="center">---[![Database Backup](https://github.com/chautdat/pdq-restaurant/actions/workflows/backup.yml/badge.svg)](https://github.com/chautdat/pdq-restaurant/actions/workflows/backup.yml)

<strong>A modern full-stack restaurant food ordering platform</strong>

</p>[![Test CI](https://github.com/chautdat/pdq-restaurant/actions/workflows/test-simple.yml/badge.svg)](https://github.com/chautdat/pdq-restaurant/actions/workflows/test-simple.yml)

<p align="center">## 📋 Table of Contents

  <a href="https://github.com/chautdat/pdq-restaurant/actions/workflows/ci.yml">

    <img src="https://github.com/chautdat/pdq-restaurant/actions/workflows/ci.yml/badge.svg" alt="CI - Build & Test"/>## 🚀 Features

  </a>

<a href="https://github.com/chautdat/pdq-restaurant/actions/workflows/cd.yml">- [Project Overview](#-project-overview)

    <img src="https://github.com/chautdat/pdq-restaurant/actions/workflows/cd.yml/badge.svg" alt="CD - Deploy"/>

</a>- [Technology Stack](#-technology-stack)- 🛒 **Shopping Cart & Checkout** with geo-location support

  <a href="https://github.com/chautdat/pdq-restaurant/actions/workflows/backup.yml">

    <img src="https://github.com/chautdat/pdq-restaurant/actions/workflows/backup.yml/badge.svg" alt="Database Backup"/>- [System Architecture](#-system-architecture)- 💳 **Payment Integration** (VNPay, ZaloPay)

  </a>

</p>- [Features](#-features)- 🎟️ **Promotional Code System** (PERCENTAGE, FIXED_AMOUNT, FREE_SHIPPING)

<p align="center">  - [Customer Features](#customer-features)- 🚚 **Distance-based Shipping Fee Calculation** (Haversine formula)

  <img src="https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat&logo=springboot&logoColor=white" alt="Spring Boot"/>

<img src="https://img.shields.io/badge/Vue.js-3.x-4FC08D?style=flat&logo=vuedotjs&logoColor=white" alt="Vue.js"/> - [Admin Features](#admin-features)- 🤖 **AI Chatbot Support** (Claude AI + Groq)

  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat&logo=mysql&logoColor=white" alt="MySQL"/>

<img src="https://img.shields.io/badge/Redis-7.x-DC382D?style=flat&logo=redis&logoColor=white" alt="Redis"/> - [Payment Integration](#payment-integration)- 📧 **Email Queue System** (RabbitMQ)

  <img src="https://img.shields.io/badge/RabbitMQ-3.12-FF6600?style=flat&logo=rabbitmq&logoColor=white" alt="RabbitMQ"/>

<img src="https://img.shields.io/badge/Docker-Ready-2496ED?style=flat&logo=docker&logoColor=white" alt="Docker"/> - [Real-time Features](#real-time-features)- 🔔 **Real-time Order Tracking** (WebSocket)

</p>

- [AI Integration](#ai-integration)- 📊 **Admin Dashboard** with real-time analytics

---

- [Database Schema](#-database-schema) - 📈 Order management with advanced filtering

## 📋 Table of Contents

- [API Endpoints](#-api-endpoints) - 🎟️ Promo code management interface

- [Overview](#-overview)

- [Features](#-features)- [Message Queue System](#-message-queue-system-rabbitmq) - 👥 User & staff management

- [Tech Stack](#-tech-stack)

- [Architecture](#-architecture)- [Deployment](#-deployment)- 🐳 **Docker Deployment**

- [Quick Start](#-quick-start)

- [API Documentation](#-api-documentation)- 🚀 **CI/CD Pipeline** with GitHub Actions

- [Database Schema](#-database-schema)

- [Message Queue System](#-message-queue-system)---

- [Payment Integration](#-payment-integration)

- [Shipping Fee Calculation](#-shipping-fee-calculation)## 🏗️ Tech Stack

- [Promotional System](#-promotional-system)

- [Deployment](#-deployment)## 🎯 Project Overview

- [Contributing](#-contributing)

- [License](#-license)### Backend

---**PDQ Restaurant** is a comprehensive food ordering platform designed for restaurants to manage their online presence, handle customer orders, and streamline operations. The system supports both customer-facing ordering and administrative management functionalities.

## 🎯 Overview- ☕ **Spring Boot 3.x**

**PDQ Restaurant** is a comprehensive food ordering platform designed for restaurants to manage their online presence, handle customer orders, and streamline operations. The system supports both customer-facing ordering and administrative management functionalities.### Key Highlights- 🗄️ **MySQL 8.0**

### ✨ Key Highlights- 🔴 **Redis** (Caching)

| Feature | Description |- **Multi-payment Gateway Support**: VNPay and ZaloPay integration- 🐰 **RabbitMQ** (Message Queue)

|---------|-------------|

| 🛒 **Smart Ordering** | Shopping cart with geo-location support |- **Real-time Order Tracking**: WebSocket-based live updates- 🔌 **WebSocket** (STOMP)

| 💳 **Multi-Payment** | VNPay & ZaloPay integration |

| 🤖 **AI Chatbot** | Claude AI & Groq powered support |- **AI-Powered Chatbot**: Intelligent customer support using Claude AI and Groq- 🔐 **JWT Authentication**

| 📧 **Email Queue** | RabbitMQ async email delivery |

| 🔔 **Real-time** | WebSocket order tracking |- **Message Queue System**: RabbitMQ for reliable email delivery and background processing- 📧 **Email Service**

| 🚚 **Smart Shipping** | Distance-based fee calculation |

| 🎟️ **Promo System** | Flexible discount codes |- **Geo-location Services**: Distance-based shipping fee calculation

| 📊 **Analytics** | Real-time admin dashboard |

- **Promotional System**: Flexible discount codes with multiple types### Frontend

---

---- 🎨 **Vue.js 3**

## 🚀 Features

- 🎯 **Axios**

### 👤 Customer Features

## 🛠 Technology Stack- 💅 **Tailwind CSS**

| Category | Features |

|----------|----------|- 🍬 **SweetAlert2**

| **Menu & Browsing** | Browse by category, search, filter by price/availability, quick view |

| **Shopping Cart** | Add/remove items, quantity update, cart persistence, special notes |### Backend

| **Checkout** | Multiple addresses, geo-location, distance-based shipping, promo codes |

| **Payment** | Cash on Delivery, VNPay, ZaloPay || Technology | Version | Purpose |### DevOps

| **Order Tracking** | Real-time status, order history, cancellation |

| **Account** | Registration, JWT auth, profile management ||------------|---------|---------|

| **AI Support** | 24/7 chatbot, menu recommendations, order assistance |

| Spring Boot | 3.x | Main backend framework |- 🐳 **Docker & Docker Compose**

### 👨‍💼 Admin Features

| MySQL | 8.0 | Primary database |- 🚀 **GitHub Actions CI/CD**

| Category | Features |

|----------|----------|| Redis | 7.x | Caching and session management |- 🌐 **Nginx**

| **Dashboard** | Real-time analytics, revenue tracking, live notifications |

| **Menu Management** | Product CRUD, categories, images, pricing || RabbitMQ | 3.12 | Message queue for async processing |

| **Order Management** | Status updates, filtering, date range, receipts |

| **Promo Codes** | Create/manage codes, usage tracking, validity periods || WebSocket (STOMP) | - | Real-time communication |### AI Integration

| **User Management** | Role management, account status, activity tracking |

| **Kitchen Display** | Real-time orders, preparation queue, completion tracking || JWT | - | Authentication and authorization |

| **Reports** | Sales reports, best-sellers, payment breakdown |

- �� **Claude AI** (Anthropic API)

---

### Frontend- 🧠 **Groq AI** (Fast inference)

## 🛠 Tech Stack

| Technology | Version | Purpose |- 🔍 **Real-time Menu Search**

### Backend

````|------------|---------|---------|

☕ Spring Boot 3.x      - Main framework

🗄️ MySQL 8.0           - Primary database| Vue.js | 3.x | Frontend framework |## 📦 Quick Start

🔴 Redis 7.x           - Caching & sessions

🐰 RabbitMQ 3.12       - Message queue| Axios | - | HTTP client |

🔌 WebSocket (STOMP)   - Real-time communication

🔐 JWT                 - Authentication| Tailwind CSS | - | Styling |### Option 1: Auto Setup (Recommended)

📧 JavaMail            - Email service

```| SweetAlert2 | - | User notifications |



### Frontend| Vuex | - | State management |```bash

````

🎨 Vue.js 3 - Frontend framework# Clone repository

🎯 Vuex - State management

📡 Axios - HTTP client### DevOps & Infrastructuregit clone https://github.com/chautdat/pdq-restaurant.git

💅 Tailwind CSS - Styling

🍬 SweetAlert2 - Notifications| Technology | Purpose |cd pdq-restaurant

````

|------------|---------|

### AI Services

```| Docker & Docker Compose | Containerization |# Run setup wizard

🧠 Claude AI (Anthropic)  - Primary chatbot

⚡ Groq AI                - Fast inference backup| Nginx | Reverse proxy and static file serving |./setup.sh

````

| GitHub Actions | CI/CD pipeline |

### DevOps

```| Ngrok | Webhook tunneling for payment callbacks |# Start Docker

🐳 Docker & Compose    - Containerization

🔄 GitHub Actions      - CI/CD pipelinecd docker

🌐 Nginx               - Reverse proxy

🔗 Ngrok               - Payment webhooks### AI Servicesdocker-compose up -d

```

| Service | Purpose |```

---

|---------|---------|

## 🏗 Architecture

| Claude AI (Anthropic) | Primary chatbot intelligence |### Option 2: Manual Setup

`````

┌─────────────────────────────────────────────────────────────────────┐| Groq AI | Fast inference backup |

│                         NGINX (Port 8080)                           │

│                    Reverse Proxy + Static Files                     │````bash

└─────────────────────────────────────────────────────────────────────┘

                                  │---# Clone repository

                    ┌─────────────┴─────────────┐

                    │                           │git clone https://github.com/chautdat/pdq-restaurant.git

                    ▼                           ▼

┌─────────────────────────┐     ┌─────────────────────────────────────┐## 🏗 System Architecturecd pdq-restaurant/docker

│   Vue.js Frontend       │     │   Spring Boot Backend (Port 3000)   │

│   ├─ Customer Portal    │     │   ├─ REST API                       │

│   ├─ Admin Dashboard    │     │   ├─ WebSocket Server               │

│   └─ Real-time Updates  │     │   └─ Payment Processing             │```# Copy environment template

└─────────────────────────┘     └─────────────────────────────────────┘

                                              │┌─────────────────────────────────────────────────────────────────┐cp .env.example .env

              ┌───────────┬───────────┬───────┴───────┬───────────┐

              │           │           │               │           ││                         NGINX (Port 8080)                        │

              ▼           ▼           ▼               ▼           ▼

        ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐│                    Reverse Proxy + Static Files                  │# Edit .env file with your API keys

        │  MySQL   │ │  Redis   │ │ RabbitMQ │ │  VNPay   │ │ ZaloPay  │

        │   :3306  │ │   :6379  │ │   :5672  │ │ Gateway  │ │ Gateway  │└─────────────────────────────────────────────────────────────────┘nano .env

        └──────────┘ └──────────┘ └──────────┘ └──────────┘ └──────────┘

```                                  │



---                    ┌─────────────┴─────────────┐# Start Docker



## 📦 Quick Start                    │                           │docker-compose up -d



### Prerequisites                    ▼                           ▼```



- Docker & Docker Compose┌─────────────────────────┐     ┌─────────────────────────────────┐

- 4GB RAM minimum

- Ports: 8080, 3000, 3306, 6379, 5672, 15672│   Vue.js Frontend       │     │   Spring Boot Backend (Port 3000)│## 🌐 Access URLs



### Option 1: Auto Setup (Recommended)│   - Customer Portal     │     │   - REST API                     │



```bash│   - Admin Dashboard     │     │   - WebSocket Server             │After starting Docker:

# Clone repository

git clone https://github.com/chautdat/pdq-restaurant.git│   - Real-time Updates   │     │   - Payment Processing           │

cd pdq-restaurant

└─────────────────────────┘     └─────────────────────────────────┘- 🌐 **Frontend:** http://localhost:8080

# Run setup wizard

./setup.sh                                              │- ⚡ **Backend API:** http://localhost:3000



# Start services                    ┌─────────────┬───────────┼───────────┬────────┐- 🐰 **RabbitMQ Management:** http://localhost:15672

cd docker

docker-compose up -d                    │             │           │           │        │  - Username: `admin`

`````

                    ▼             ▼           ▼           ▼        ▼  - Password: `admin123`

### Option 2: Manual Setup

              ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐

`````bash

# Clone repository              │  MySQL   │ │  Redis   │ │ RabbitMQ │ │  VNPay   │ │ ZaloPay  │## 🎯 CI/CD Pipeline

git clone https://github.com/chautdat/pdq-restaurant.git

cd pdq-restaurant/docker              │ Database │ │  Cache   │ │  Queue   │ │ Gateway  │ │ Gateway  │



# Configure environment              └──────────┘ └──────────┘ └──────────┘ └──────────┘ └──────────┘Automated pipeline with GitHub Actions:

cp .env.example .env

nano .env  # Edit with your API keys````



# Start services- ✅ **CI**: Build & Test on every push

docker-compose up -d

```---- ✅ **CD**: Auto deploy to production



### 🌐 Access URLs- ✅ **Backup**: Daily database backups



| Service | URL | Credentials |## ✨ Features- ✅ **Quality**: Code quality checks

|---------|-----|-------------|

| Frontend | http://localhost:8080 | - |### Customer Features## 🎟️ Promotional Code System

| Backend API | http://localhost:3000 | - |

| RabbitMQ UI | http://localhost:15672 | admin / admin123 |#### 🏠 Homepage### Features

| Ngrok Inspector | http://localhost:4040 | - |

- Featured products showcase

---

- Promotional banners- **3 Discount Types:**

## 🔌 API Documentation

- Quick navigation to menu categories - � **PERCENTAGE**: Percentage discount with max cap

### Authentication

- Restaurant information display - 💵 **FIXED_AMOUNT**: Fixed amount discount

| Method | Endpoint | Description |

|--------|----------|-------------|  - 🚚 **FREE_SHIPPING**: Free shipping cost

| `POST` | `/api/auth/register` | User registration |

| `POST` | `/api/auth/login` | User login |#### 🍔 Menu & Product Browsing

| `POST` | `/api/auth/verify` | Email verification |

| `POST` | `/api/auth/refresh` | Refresh JWT token |- Browse products by category (Rice, Noodles, Drinks, Desserts, etc.)### Admin Management



### Products- Product search functionality



| Method | Endpoint | Description |- Filter by price range```vue

|--------|----------|-------------|

| `GET` | `/api/products` | List all products |- Filter by availability status/admin/promo-codes → PromoCodeManager.vue - Create/Edit/Delete promo codes -

| `GET` | `/api/products/{id}` | Get product details |

| `POST` | `/api/products` | Create product (Admin) |- Product quick view with detailed informationToggle active/inactive status - View usage statistics - Set validity date ranges

| `PUT` | `/api/products/{id}` | Update product (Admin) |

| `DELETE` | `/api/products/{id}` | Delete product (Admin) |- Product images and descriptions- Configure usage limits



### Cart````



| Method | Endpoint | Description |#### 🛒 Shopping Cart

|--------|----------|-------------|

| `GET` | `/api/cart` | Get user's cart |- Add/remove items from cart### Promo Code Example

| `POST` | `/api/cart/items` | Add item to cart |

| `PUT` | `/api/cart/items/{id}` | Update cart item |- Update item quantities

| `DELETE` | `/api/cart/items/{id}` | Remove cart item |

- Real-time price calculation```json

### Orders

- Cart persistence across sessions{

| Method | Endpoint | Description |

|--------|----------|-------------|- Item notes for special requests  "code": "WELCOME25",

| `GET` | `/api/orders` | List user's orders |

| `GET` | `/api/orders/{id}` | Get order details |  "type": "PERCENTAGE",

| `POST` | `/api/orders` | Create new order |

| `PUT` | `/api/orders/{id}/status` | Update status (Admin) |#### 📝 Checkout Process  "value": 25,

| `POST` | `/api/orders/{id}/cancel` | Cancel order |

- Multiple delivery address support  "maxDiscountAmount": 500000,

### Payments

- Address auto-complete with geo-location  "minAmount": 200000,

| Method | Endpoint | Description |

|--------|----------|-------------|- Distance-based shipping fee calculation (Haversine formula)  "usageLimit": 100,

| `POST` | `/api/payment/vnpay/create` | Create VNPay payment |

| `GET` | `/api/payment/vnpay/callback` | VNPay callback |- Promo code application  "usedCount": 42,

| `POST` | `/api/payment/zalopay/create` | Create ZaloPay payment |

| `POST` | `/api/payment/zalopay/callback` | ZaloPay callback |- Multiple payment method selection  "active": true,



### Promo Codes- Order summary review  "startDate": "2024-01-01",



| Method | Endpoint | Description |  "endDate": "2024-12-31"

|--------|----------|-------------|

| `GET` | `/api/promo-codes/validate/{code}` | Validate promo code |#### 💳 Payment Options}

| `GET` | `/api/promo-codes` | List all codes (Admin) |

| `POST` | `/api/promo-codes` | Create code (Admin) |- **Cash on Delivery (COD)**: Pay when order arrives```

| `PUT` | `/api/promo-codes/{id}` | Update code (Admin) |

| `DELETE` | `/api/promo-codes/{id}` | Delete code (Admin) |- **VNPay**: Vietnamese e-wallet and bank transfer



---- **ZaloPay**: Popular Vietnamese payment gateway### Auto-generated Signup Code



## 🗄 Database Schema



### Core Tables#### 📦 Order ManagementWhen a user registers, they automatically receive a **SIGNUP\_[userId]** code with 20% discount.



| Table | Description |- View order history

|-------|-------------|

| `users` | Customer and admin accounts |- Real-time order status tracking---

| `products` | Menu items with prices |

| `categories` | Product categorization |- Order cancellation (within time limit)

| `carts` | Shopping cart storage |

| `cart_items` | Individual cart items |- Payment retry for failed transactions## 🚚 Shipping Fee Calculation

| `orders` | Order records |

| `order_items` | Order line items |- Order details with itemized breakdown

| `promo_codes` | Discount codes |

| `payments` | Transaction records |### Distance-Based Pricing (Haversine Formula)

| `addresses` | Delivery addresses |

#### 👤 User Account

### Promo Codes Schema

- User registration with email verification```

```sql

CREATE TABLE promo_codes (- Secure login with JWT authenticationBase: 15,000 VND (0-3 km)

    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    code VARCHAR(50) UNIQUE NOT NULL,- Profile managementExtended: 5,000 VND per km (> 3 km)

    type ENUM('PERCENTAGE', 'FIXED_AMOUNT', 'FREE_SHIPPING') NOT NULL,

    value DECIMAL(10,2) NOT NULL,- Password change functionality

    max_discount_amount BIGINT,

    min_amount BIGINT,- Order history accessExamples:

    usage_limit INT,

    used_count INT DEFAULT 0,- 2 km → 15,000 VND

    active BOOLEAN DEFAULT true,

    start_date DATE,#### 🎟️ Promotional System- 3 km → 15,000 VND

    end_date DATE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP- Apply discount codes at checkout- 5 km → 25,000 VND (15,000 + 2×5,000)

);

```- Multiple promo types supported:- 10 km → 50,000 VND (15,000 + 7×5,000)



---  - **PERCENTAGE**: Percentage discount (e.g., 10% off)```



## 📨 Message Queue System  - **FIXED_AMOUNT**: Fixed amount discount (e.g., 50,000 VND off)



### RabbitMQ Configuration  - **FREE_SHIPPING**: Free delivery fee### Coordinates Required



| Setting | Value |- Minimum order value requirements

|---------|-------|

| Host | localhost (Docker: rabbitmq) |- Usage limits per codeFrontend must send:

| AMQP Port | 5672 |

| Management Port | 15672 |- Expiration date validation

| Username | admin |

| Password | admin123 |```json



### Implemented Queues#### 🤖 AI Chatbot Support{



#### 📧 Email Queue (`email.queue`)- 24/7 intelligent customer support  "deliveryLat": 10.7769,



Handles asynchronous email delivery:- Menu recommendations  "deliveryLng": 106.7009,

- Order confirmation emails

- Payment notifications- Order assistance  ...

- Account verification

- Password reset emails- FAQ responses}

- Promotional campaigns

- Natural language understanding```

**Benefits:**

- Non-blocking email sending

- Automatic retry on failure

- Scalable processing#### 🪑 Table ReservationBackend validates and calculates distance from restaurant coordinates using **Haversine formula**.



#### 📦 Order Queue (`order.queue`)- View available tables



Background order processing:- Reserve tables for dine-in### FREE_SHIPPING Override

- Status updates

- Inventory management- Table capacity information

- Kitchen notifications

- Delivery assignmentIf promo code is `FREE_SHIPPING` type, shipping fee is set to **0 VND**.



#### 🔔 Notification Queue (`notification.queue`)---



Real-time alerts:---

- WebSocket broadcasts

- Push notifications### Admin Features

- Admin alerts

## � **Chi Tiết Thuật Toán Tính Toán**

### Message Flow

#### 📊 Dashboard

`````

1. Event Triggered → Backend publishes to queue- Real-time analytics and statistics### 1️⃣ **Thuật Toán Tính Phí Giao Hàng (Shipping Fee)**

2. RabbitMQ stores message reliably

3. Consumer picks up message- Order count by status

4. Service processes message

5. Success → Message acknowledged- Revenue tracking (daily/weekly/monthly)#### **Công Thức Haversine - Tính Khoảng Cách**

6. Failure → Message requeued for retry

````- Top-selling products



---- Customer statistics```



## 💳 Payment Integration- Live order notificationsCông thức:



### Supported Gatewaysa = sin²(Δlat/2) + cos(lat1) × cos(lat2) × sin²(Δlng/2)



| Gateway | Features |#### 🍽️ Menu Managementc = 2 × atan2(√a, √(1−a))

|---------|----------|

| **VNPay** | QR code, bank transfer, e-wallet, auto-verification |- Add new products with imagesdistance = R × c

| **ZaloPay** | E-wallet, QR code, real-time status |

| **COD** | Cash on delivery |- Edit product information



### Payment Flow- Set product availabilityTrong đó:



```- Manage product categories- lat1, lng1: Vĩ độ, kinh độ nhà hàng (Restaurant coordinates)

1. Customer selects payment method

2. System generates payment URL/QR- Upload product images- lat2, lng2: Vĩ độ, kinh độ giao hàng (Delivery coordinates)

3. Customer completes payment

4. Gateway sends callback- Set prices and descriptions- R: Bán kính Trái Đất = 6,371 km

5. Backend verifies & updates order

6. Customer receives confirmation- Δlat, Δlng: Chênh lệch vĩ độ, kinh độ

````

#### 📁 Category Management```

---

- Create product categories

## 🚚 Shipping Fee Calculation

- Edit category names and icons#### **Ví Dụ Tính Toán:**

### Haversine Formula

- Organize menu structure

Distance-based pricing using GPS coordinates:

- Category display order**Nhà hàng:** lat=10.7769, lng=106.7009 (Quận 1, TP.HCM)

`````

Base Fee: 15,000 VND (0-3 km)

Extended: +5,000 VND per km (> 3 km)

```#### 📋 Order Management```javascript



### Pricing Examples- View all orders with filtering// Haversine Implementation (Backend - Java)



| Distance | Calculation | Fee |- Filter by status (Pending, Confirmed, Preparing, Delivering, Completed, Cancelled)private double haversineDistanceKm(double lat1, double lng1, double lat2, double lng2) {

|----------|-------------|-----|

| 2 km | Base rate | 15,000 VND |- Filter by date range  double R = 6371; // Bán kính Trái Đất (km)

| 3 km | Base rate | 15,000 VND |

| 5 km | 15,000 + (2 × 5,000) | 25,000 VND |- Filter by payment status

| 10 km | 15,000 + (7 × 5,000) | 50,000 VND |

- Update order status  double latDistance = Math.toRadians(lat2 - lat1);

---

- View order details  double lngDistance = Math.toRadians(lng2 - lng1);

## 🎟 Promotional System

- Print order receipts

### Discount Types

  double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +

| Type | Description | Example |

|------|-------------|---------|#### 🎟️ Promo Code Management             Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *

| `PERCENTAGE` | Percentage off with max cap | 25% off, max 500,000 VND |

| `FIXED_AMOUNT` | Fixed amount discount | 50,000 VND off |- Create new promotional codes             Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

| `FREE_SHIPPING` | Free delivery | 0 VND shipping |

- Set discount type and value

### Promo Code Example

- Configure usage limits  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

```json

{- Set validity period

    "code": "WELCOME25",

    "type": "PERCENTAGE",- Enable/disable codes  return R * c; // Khoảng cách theo km

    "value": 25,

    "maxDiscountAmount": 500000,- Track code usage statistics}

    "minAmount": 200000,

    "usageLimit": 100,````

    "active": true,

    "startDate": "2024-01-01",#### 👥 User Management

    "endDate": "2024-12-31"

}- View all registered users#### **Bảng Giá Phí Giao Hàng:**

`````

- User role management (Customer, Staff, Admin)

### Auto-generated Signup Code

- Account status management| Khoảng Cách | Công Thức | Phí Giao Hàng |

New users automatically receive a `SIGNUP_[userId]` code with 20% discount.

- User activity tracking| ------------- | ------------------------------- | -------------- |

---

| **0 - 3 km** | Phí cố định | **15,000 VND** |

## 🐳 Deployment

#### 👨‍🍳 Kitchen Display System (KDS)| **3 - 4 km** | 15,000 + (distance - 3) × 5,000 | **20,000 VND** |

### Docker Services (7 Containers)

- Real-time incoming orders| **4 - 5 km** | 15,000 + (distance - 3) × 5,000 | **25,000 VND** |

| Service | Port | Description |

|---------|------|-------------|- Order preparation queue| **5 - 10 km** | 15,000 + (distance - 3) × 5,000 | **50,000 VND** |

| pdq-frontend | 8080 | Vue.js + Nginx |

| pdq-backend | 3000 | Spring Boot API |- Mark items as prepared| **10+ km** | 15,000 + (distance - 3) × 5,000 | Tính động |

| pdq-mysql | 3306 | MySQL 8.0 |

| pdq-redis | 6379 | Redis cache |- Order completion tracking

| pdq-rabbitmq | 5672, 15672 | Message broker |

| pdq-ngrok | 4040 | Webhook tunnel |#### **Thuật Toán Chi Tiết:**

### Environment Variables#### 💰 Sales & Revenue Reports

| Variable | Description |- Daily sales reports```java

|----------|-------------|

| `MYSQL_ROOT_PASSWORD` | Database password |- Revenue analyticspublic int calculateShippingFee(double distanceKm, boolean hasFreeShippingPromo) {

| `JWT_SECRET` | JWT signing key |

| `VNPAY_TMN_CODE` | VNPay merchant code |- Best-selling products report // 1. Kiểm tra FREE_SHIPPING promo

| `VNPAY_HASH_SECRET` | VNPay secret |

| `ZALOPAY_APP_ID` | ZaloPay app ID |- Payment method breakdown if (hasFreeShippingPromo) {

| `CLAUDE_API_KEY` | Anthropic API key |

| `GROQ_API_KEY` | Groq API key | return 0; // Miễn phí vận chuyển

| `RABBITMQ_USERNAME` | RabbitMQ user |

| `RABBITMQ_PASSWORD` | RabbitMQ password |--- }

### Docker Commands### Payment Integration // 2. Đảm bảo khoảng cách tối thiểu = 1 km (nếu Haversine trả về 0)

````bashif (distanceKm <= 0) {

# Start all services

docker-compose up -d#### VNPay Integration distanceKm = 1.0;



# View logs- Secure payment processing }

docker-compose logs -f

- QR code payment support

# Rebuild and start

docker-compose up -d --build- Bank transfer support // 3. Tính phí dựa trên khoảng cách



# Stop all services- Automatic payment verification if (distanceKm <= 3) {

docker-compose down

- Payment callback handling return 15000; // Phí cố định 0-3 km

# Full cleanup

docker-compose down -v- Refund processing capability } else {

docker rmi docker-backend docker-frontend

```  // Phí cơ bản + phí bổ sung theo khoảng cách vượt quá 3 km



### Health Check#### ZaloPay Integration int additionalFee = (int) Math.round((distanceKm - 3) \* 5000);



```bash- E-wallet payment return 15000 + additionalFee;

# Backend health

curl http://localhost:3000/actuator/health- QR code generation }



# Frontend- Real-time payment status}

curl http://localhost:8080

```- Automatic order confirmation



---// Ước tính thời gian giao hàng



## 🔒 Security#### Payment Flowprivate int estimateDeliveryTime(double distanceKm) {



- ✅ JWT authentication1. Customer selects payment method at checkout // Giả định tốc độ bình quân: 30 km/h

- ✅ API keys in `.env` (not committed)

- ✅ CORS configuration2. System generates payment URL/QR code return (int) Math.round((distanceKm / 30.0) \* 60); // Trả về phút

- ✅ Input validation

- ✅ SQL injection prevention3. Customer completes payment on gateway}

- ✅ Rate limiting on promo codes

- ✅ Coordinate validation4. Gateway sends callback to backend```



---5. Backend verifies and updates order status



## 🤝 Contributing6. Customer receives confirmation#### **Ví Dụ Cụ Thể:**



1. Fork the repository---```

2. Create feature branch (`git checkout -b feature/amazing-feature`)

3. Commit changes (`git commit -m 'Add amazing feature'`)📍 Địa chỉ giao: Quận 7, TP.HCM (lat=10.7234, lng=106.7346)

4. Push to branch (`git push origin feature/amazing-feature`)

5. Open a Pull Request### Real-time Features



---Bước 1: Tính khoảng cách Haversine



## 📧 Contact#### WebSocket Implementation Nhà hàng (10.7769, 106.7009) → Địa chỉ (10.7234, 106.7346)



**Author:** Châu Tấn Đạt  - **Order Status Updates**: Customers receive instant notifications when order status changes Khoảng cách = 6.2 km

**GitHub:** [@chautdat](https://github.com/chautdat)

- **Kitchen Notifications**: New orders appear instantly on kitchen display

---

- **Admin Dashboard**: Live statistics and order feedBước 2: Tính phí giao hàng

## 📝 License

- **Chat Support**: Real-time messaging with AI chatbot Vì 6.2 km > 3 km:

This project is developed for educational purposes as part of a university capstone project.

  Phí = 15,000 + (6.2 - 3) × 5,000

---

#### Notification Types = 15,000 + 3.2 × 5,000

<p align="center">

  ⭐ <strong>Star this repo if you find it helpful!</strong> ⭐- Order confirmation = 15,000 + 16,000

</p>

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

| Setting | Value |│ 2× Cơm Gà Hainaam @ 85,000 VND │

|---------|-------|│ 1× Nước Cam @ 15,000 VND │

| Host | localhost (Docker: rabbitmq) |│ SUBTOTAL: 180,000 VND │

| AMQP Port | 5672 |└─────────────────────────────────────────┘

| Management Port | 15672 |

| Virtual Host | / |💳 PROMO CODE: "WELCOME25"

| Default Username | admin | Type: PERCENTAGE

| Default Password | admin123 | Value: 25%

Max Discount: 500,000 VND

### Implemented Queues Min Amount: 150,000 VND ✓ (180,000 ≥ 150,000)

#### 1. Email Queue (`email.queue`)🧮 TÍNH KHUYẾN MÃI:

**Purpose**: Asynchronous email delivery for order confirmations, notifications, and promotional emails. Discount = 180,000 × (25 / 100) = 45,000 VND

(45,000 < 500,000, nên áp dụng 45,000)

**Message Types**:

- Order confirmation emails🚚 PHƯƠNG TIỆN GIAO:

- Payment success/failure notifications Distance = 4.5 km

- Account verification emails Shipping = 15,000 + (4.5 - 3) × 5,000 = 22,500 VND

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

**Purpose**: Handle order-related background tasks.│ 3× Phở Bò Tái Nam @ 65,000 VND │

│ 1× Nước Chanh @ 12,000 VND │

**Operations**:│ SUBTOTAL: 207,000 VND │

- Order status updates└─────────────────────────────────────────┘

- Inventory updates

- Kitchen notification dispatch💳 PROMO CODE: "SHIPPING2024"

- Delivery assignment Type: FREE_SHIPPING

  (Tự động miễn phí vận chuyển)

#### 3. Notification Queue (`notification.queue`)

**Purpose**: Push notifications and real-time alerts.🧮 TÍNH KHUYẾN MÃI:

Discount = 0 VND (FREE_SHIPPING không giảm giá sản phẩm)

**Message Types**:

- WebSocket broadcasts🚚 PHƯƠNG TIỆN GIAO:

- Mobile push notifications Distance = 8.2 km

- Admin alerts Normal Shipping = 15,000 + (8.2 - 3) × 5,000 = 41,000 VND

  ❌ NHƯNG có FREE_SHIPPING promo

### Message Flow Example (Email) ✅ Actual Shipping = 0 VND (Giảm 41,000 VND)

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
