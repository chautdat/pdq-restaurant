# PDQ Restaurant

A modern full-stack food ordering system for restaurants with online payment integration, real-time order tracking, and AI-powered customer support.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Vue.js](https://img.shields.io/badge/Vue.js-3.x-42b883)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED)
![License](https://img.shields.io/badge/License-Educational-yellow)

---

## Overview

PDQ Restaurant is a comprehensive food ordering platform that enables restaurants to manage online orders, process payments, and deliver excellent customer service. The system features a customer-facing web application and a powerful admin dashboard.

---

## Tech Stack

| Layer | Technologies |
|-------|-------------|
| **Backend** | Spring Boot 3.x, Spring Security, JWT, JPA/Hibernate |
| **Database** | MySQL 8.0, Redis (caching) |
| **Message Queue** | RabbitMQ (async email, notifications) |
| **Frontend** | Vue.js 3, Vuex, Vue Router, Axios |
| **Styling** | Tailwind CSS, SweetAlert2 |
| **Real-time** | WebSocket (STOMP protocol) |
| **AI** | Claude AI (Anthropic), Groq AI |
| **Payment** | VNPay, ZaloPay |
| **DevOps** | Docker, Docker Compose, Nginx, GitHub Actions |

---

## Features

### Customer Portal

| Feature | Description |
|---------|-------------|
| **Menu Browsing** | Browse products by category, search, filter by price |
| **Shopping Cart** | Add/remove items, update quantities, special notes |
| **Smart Checkout** | Address autocomplete with geo-location, distance-based shipping |
| **Online Payment** | VNPay (QR/Bank), ZaloPay, Cash on Delivery |
| **Order Tracking** | Real-time status updates via WebSocket |
| **Promo Codes** | Percentage discount, fixed amount, free shipping |
| **AI Chatbot** | 24/7 support, menu recommendations, order assistance |
| **User Account** | Registration, email verification, order history |
| **Table Booking** | Reserve tables for dine-in |

### Admin Dashboard

| Feature | Description |
|---------|-------------|
| **Analytics** | Real-time sales, revenue charts, top products |
| **Menu Management** | CRUD products, categories, availability toggle |
| **Order Management** | View/filter orders, update status, print receipts |
| **Promo Codes** | Create/manage discount codes with usage limits |
| **User Management** | Manage customers, staff roles, account status |
| **Kitchen Display** | Real-time order queue for kitchen staff |
| **Reports** | Daily/weekly/monthly sales reports |

---

## System Architecture

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   Frontend   │────▶│   Backend    │────▶│    MySQL     │
│  Vue.js 3    │     │ Spring Boot  │     │   Database   │
│  (Nginx)     │     │    (API)     │     └──────────────┘
└──────────────┘     └──────┬───────┘              
     :8080                  │:3000           
                           ├─────────────▶ Redis (Cache)
                           │               :6379
                           │
                           ├─────────────▶ RabbitMQ (Queue)
                           │               :5672
                           │
                           └─────────────▶ Payment Gateways
                                          (VNPay, ZaloPay)
```

---

## Services

| Service | Port | Description |
|---------|------|-------------|
| **Frontend** | 8080 | Vue.js application (Nginx) |
| **Backend** | 3000 | Spring Boot REST API |
| **MySQL** | 3306 | Primary database |
| **Redis** | 6379 | Session & data caching |
| **RabbitMQ** | 5672 | Message broker (AMQP) |
| **RabbitMQ UI** | 15672 | Management console |
| **Ngrok** | 4040 | Payment webhook tunnel |

---

## Message Queue (RabbitMQ)

Handles asynchronous background tasks:

- **Email Queue**: Order confirmations, payment receipts, verification emails
- **Notification Queue**: Real-time WebSocket broadcasts, push alerts
- **Order Queue**: Inventory updates, kitchen notifications

---

## Payment Integration

| Gateway | Features |
|---------|----------|
| **VNPay** | QR code payment, bank transfer, automatic verification |
| **ZaloPay** | E-wallet payment, instant confirmation |
| **COD** | Cash on delivery option |

---

## Quick Start

### Prerequisites
- Docker & Docker Compose
- Git

### Installation

```bash
# Clone repository
git clone https://github.com/chautdat/pdq-restaurant.git
cd pdq-restaurant

# Start all services
cd docker
docker-compose up -d

# Check status
docker-compose ps
```

### Access URLs

| Service | URL | Credentials |
|---------|-----|-------------|
| Frontend | http://localhost:8080 | - |
| Backend API | http://localhost:3000 | - |
| RabbitMQ | http://localhost:15672 | admin / admin123 |

---

## Project Structure

```
pdq-restaurant/
├── frontend/           # Vue.js application
│   ├── src/
│   │   ├── pages/      # Customer pages
│   │   ├── admin/      # Admin dashboard
│   │   ├── components/ # Reusable components
│   │   └── store/      # Vuex state management
│   └── public/
│
├── pdq-BE/             # Spring Boot backend
│   └── src/main/java/com/pdq/
│       ├── controller/ # REST endpoints
│       ├── service/    # Business logic
│       ├── entity/     # Database entities
│       └── repository/ # Data access
│
├── docker/             # Docker configuration
│   ├── docker-compose.yml
│   └── nginx.conf
│
└── uploads/            # Uploaded images
```

---

## API Endpoints

### Authentication
```
POST /api/auth/register    - User registration
POST /api/auth/login       - User login
POST /api/auth/verify      - Email verification
```

### Products & Cart
```
GET  /api/products         - List products
POST /api/cart/items       - Add to cart
DELETE /api/cart/items/:id - Remove from cart
```

### Orders & Payments
```
POST /api/orders           - Create order
GET  /api/orders           - Order history
POST /api/payment/vnpay    - VNPay payment
POST /api/payment/zalopay  - ZaloPay payment
```

---

## Environment Variables

| Variable | Description |
|----------|-------------|
| `MYSQL_ROOT_PASSWORD` | Database password |
| `JWT_SECRET` | JWT signing key |
| `VNPAY_TMN_CODE` | VNPay merchant code |
| `VNPAY_HASH_SECRET` | VNPay secret key |
| `ZALOPAY_APP_ID` | ZaloPay app ID |
| `CLAUDE_API_KEY` | Claude AI API key |
| `GROQ_API_KEY` | Groq AI API key |
| `MAIL_USERNAME` | SMTP email |
| `MAIL_PASSWORD` | SMTP password |

---

## Screenshots

| Customer | Admin |
|----------|-------|
| Homepage | Dashboard |
| Menu | Order Management |
| Cart & Checkout | Menu Management |
| Order Tracking | Promo Codes |

---

## Author

**Chau Tan Dat**  
University Capstone Project

---

## License

This project is for educational purposes only.
