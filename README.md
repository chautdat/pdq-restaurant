# ��️ PDQ Restaurant - Food Ordering System

[![CI - Build & Test](https://github.com/chautdat/pdq-restaurant/actions/workflows/ci.yml/badge.svg)](https://github.com/chautdat/pdq-restaurant/actions/workflows/ci.yml)
[![CD - Deploy](https://github.com/chautdat/pdq-restaurant/actions/workflows/cd.yml/badge.svg)](https://github.com/chautdat/pdq-restaurant/actions/workflows/cd.yml)
[![Database Backup](https://github.com/chautdat/pdq-restaurant/actions/workflows/backup.yml/badge.svg)](https://github.com/chautdat/pdq-restaurant/actions/workflows/backup.yml)
[![Test CI](https://github.com/chautdat/pdq-restaurant/actions/workflows/test-simple.yml/badge.svg)](https://github.com/chautdat/pdq-restaurant/actions/workflows/test-simple.yml)

## 🚀 Features

- 🛒 **Shopping Cart & Checkout**
- 💳 **Payment Integration** (VNPay, ZaloPay)
- 🤖 **AI Chatbot Support** (Claude AI + Groq)
- 📧 **Email Queue System** (RabbitMQ)
- 🔔 **Real-time Order Tracking** (WebSocket)
- 📊 **Admin Dashboard** with real-time analytics
- 🐳 **Docker Deployment**
- 🚀 **CI/CD Pipeline** with GitHub Actions

## 🏗️ Tech Stack

### Backend

- ☕ **Spring Boot 3.x**
- 🗄️ **MySQL 8.0**
- 🔴 **Redis** (Caching)
- 🐰 **RabbitMQ** (Message Queue)
- 🔌 **WebSocket** (STOMP)
- 🔐 **JWT Authentication**
- 📧 **Email Service**

### Frontend

- 🎨 **Vue.js 3**
- 🎯 **Axios**
- 💅 **Tailwind CSS**
- 🍬 **SweetAlert2**

### DevOps

- 🐳 **Docker & Docker Compose**
- 🚀 **GitHub Actions CI/CD**
- 🌐 **Nginx**

### AI Integration

- �� **Claude AI** (Anthropic API)
- 🧠 **Groq AI** (Fast inference)
- 🔍 **Real-time Menu Search**

## 📦 Quick Start

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

## 🌐 Access URLs

After starting Docker:

- 🌐 **Frontend:** http://localhost:8080
- ⚡ **Backend API:** http://localhost:3000
- 🐰 **RabbitMQ Management:** http://localhost:15672
  - Username: `admin`
  - Password: `admin123`

## 🎯 CI/CD Pipeline

Automated pipeline with GitHub Actions:

- ✅ **CI**: Build & Test on every push
- ✅ **CD**: Auto deploy to production
- ✅ **Backup**: Daily database backups
- ✅ **Quality**: Code quality checks

## 📁 Project Structure

```
pdq-restaurant/
├── pdq-BE/              # Spring Boot Backend
├── frontend/            # Vue.js Frontend
├── docker/              # Docker configuration
│   ├── .env             # Environment variables (not committed)
│   ├── .env.example     # Environment template
│   └── docker-compose.yml
├── .github/workflows/   # CI/CD workflows
├── setup.sh             # Auto setup script
└── README.md
```

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
- ✅ Input validation
- ✅ SQL injection prevention

## 📧 Contact

**Author:** Châu Tấn Đạt
**Email:** chautdat@example.com
**GitHub:** [@chautdat](https://github.com/chautdat)

## 📝 License

MIT License

---

⭐ **Star this repo if you find it helpful!**
