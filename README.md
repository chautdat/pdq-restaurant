# ��️ PDQ Restaurant - Food Ordering System

[![CI - Build & Test](https://github.com/chautdat/pdq-restaurant/actions/workflows/ci.yml/badge.svg)](https://github.com/chautdat/pdq-restaurant/actions/workflows/ci.yml)
[![CD - Deploy](https://github.com/chautdat/pdq-restaurant/actions/workflows/cd.yml/badge.svg)](https://github.com/chautdat/pdq-restaurant/actions/workflows/cd.yml)
[![Database Backup](https://github.com/chautdat/pdq-restaurant/actions/workflows/backup.yml/badge.svg)](https://github.com/chautdat/pdq-restaurant/actions/workflows/backup.yml)
[![Test CI](https://github.com/chautdat/pdq-restaurant/actions/workflows/test-simple.yml/badge.svg)](https://github.com/chautdat/pdq-restaurant/actions/workflows/test-simple.yml)

## 🚀 Features

- 🛒 **Shopping Cart & Checkout** with geo-location support
- 💳 **Payment Integration** (VNPay, ZaloPay)
- 🎟️ **Promotional Code System** (PERCENTAGE, FIXED_AMOUNT, FREE_SHIPPING)
- 🚚 **Distance-based Shipping Fee Calculation** (Haversine formula)
- 🤖 **AI Chatbot Support** (Claude AI + Groq)
- 📧 **Email Queue System** (RabbitMQ)
- 🔔 **Real-time Order Tracking** (WebSocket)
- 📊 **Admin Dashboard** with real-time analytics
  - 📈 Order management with advanced filtering
  - 🎟️ Promo code management interface
  - 👥 User & staff management
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

## 🎟️ Promotional Code System

### Features

- **3 Discount Types:**
  - � **PERCENTAGE**: Percentage discount with max cap
  - 💵 **FIXED_AMOUNT**: Fixed amount discount
  - 🚚 **FREE_SHIPPING**: Free shipping cost

### Admin Management

```vue
/admin/promo-codes → PromoCodeManager.vue - Create/Edit/Delete promo codes -
Toggle active/inactive status - View usage statistics - Set validity date ranges
- Configure usage limits
```

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

### Auto-generated Signup Code

When a user registers, they automatically receive a **SIGNUP\_[userId]** code with 20% discount.

---

## 🚚 Shipping Fee Calculation

### Distance-Based Pricing (Haversine Formula)

```
Base: 15,000 VND (0-3 km)
Extended: 5,000 VND per km (> 3 km)

Examples:
- 2 km → 15,000 VND
- 3 km → 15,000 VND
- 5 km → 25,000 VND (15,000 + 2×5,000)
- 10 km → 50,000 VND (15,000 + 7×5,000)
```

### Coordinates Required

Frontend must send:

```json
{
  "deliveryLat": 10.7769,
  "deliveryLng": 106.7009,
  ...
}
```

Backend validates and calculates distance from restaurant coordinates using **Haversine formula**.

### FREE_SHIPPING Override

If promo code is `FREE_SHIPPING` type, shipping fee is set to **0 VND**.

---

## � **Chi Tiết Thuật Toán Tính Toán**

### 1️⃣ **Thuật Toán Tính Phí Giao Hàng (Shipping Fee)**

#### **Công Thức Haversine - Tính Khoảng Cách**

```
Công thức:
a = sin²(Δlat/2) + cos(lat1) × cos(lat2) × sin²(Δlng/2)
c = 2 × atan2(√a, √(1−a))
distance = R × c

Trong đó:
- lat1, lng1: Vĩ độ, kinh độ nhà hàng (Restaurant coordinates)
- lat2, lng2: Vĩ độ, kinh độ giao hàng (Delivery coordinates)
- R: Bán kính Trái Đất = 6,371 km
- Δlat, Δlng: Chênh lệch vĩ độ, kinh độ
```

#### **Ví Dụ Tính Toán:**

**Nhà hàng:** lat=10.7769, lng=106.7009 (Quận 1, TP.HCM)

```javascript
// Haversine Implementation (Backend - Java)
private double haversineDistanceKm(double lat1, double lng1, double lat2, double lng2) {
  double R = 6371; // Bán kính Trái Đất (km)

  double latDistance = Math.toRadians(lat2 - lat1);
  double lngDistance = Math.toRadians(lng2 - lng1);

  double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
             Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
             Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

  return R * c; // Khoảng cách theo km
}
```

#### **Bảng Giá Phí Giao Hàng:**

| Khoảng Cách   | Công Thức                       | Phí Giao Hàng  |
| ------------- | ------------------------------- | -------------- |
| **0 - 3 km**  | Phí cố định                     | **15,000 VND** |
| **3 - 4 km**  | 15,000 + (distance - 3) × 5,000 | **20,000 VND** |
| **4 - 5 km**  | 15,000 + (distance - 3) × 5,000 | **25,000 VND** |
| **5 - 10 km** | 15,000 + (distance - 3) × 5,000 | **50,000 VND** |
| **10+ km**    | 15,000 + (distance - 3) × 5,000 | Tính động      |

#### **Thuật Toán Chi Tiết:**

```java
public int calculateShippingFee(double distanceKm, boolean hasFreeShippingPromo) {
  // 1. Kiểm tra FREE_SHIPPING promo
  if (hasFreeShippingPromo) {
    return 0; // Miễn phí vận chuyển
  }

  // 2. Đảm bảo khoảng cách tối thiểu = 1 km (nếu Haversine trả về 0)
  if (distanceKm <= 0) {
    distanceKm = 1.0;
  }

  // 3. Tính phí dựa trên khoảng cách
  if (distanceKm <= 3) {
    return 15000; // Phí cố định 0-3 km
  } else {
    // Phí cơ bản + phí bổ sung theo khoảng cách vượt quá 3 km
    int additionalFee = (int) Math.round((distanceKm - 3) * 5000);
    return 15000 + additionalFee;
  }
}

// Ước tính thời gian giao hàng
private int estimateDeliveryTime(double distanceKm) {
  // Giả định tốc độ bình quân: 30 km/h
  return (int) Math.round((distanceKm / 30.0) * 60); // Trả về phút
}
```

#### **Ví Dụ Cụ Thể:**

```
📍 Địa chỉ giao: Quận 7, TP.HCM (lat=10.7234, lng=106.7346)

Bước 1: Tính khoảng cách Haversine
  Nhà hàng (10.7769, 106.7009) → Địa chỉ (10.7234, 106.7346)
  Khoảng cách = 6.2 km

Bước 2: Tính phí giao hàng
  Vì 6.2 km > 3 km:
  Phí = 15,000 + (6.2 - 3) × 5,000
      = 15,000 + 3.2 × 5,000
      = 15,000 + 16,000
      = 31,000 VND ✓

Bước 3: Ước tính thời gian
  Thời gian = (6.2 / 30) × 60 = 12.4 phút ≈ 12 phút
```

---

### 2️⃣ **Thuật Toán Tính Khuyến Mãi (Promotion Calculation)**

#### **3 Loại Khuyến Mãi:**

```
┌─────────────────────────────────────────────────────────────┐
│                    DISCOUNT TYPES                            │
├──────────────────┬────────────────┬──────────────────────────┤
│ PERCENTAGE       │ FIXED_AMOUNT   │ FREE_SHIPPING            │
├──────────────────┼────────────────┼──────────────────────────┤
│ Giảm theo %      │ Giảm cố định   │ Miễn phí vận chuyển      │
│ Có giới hạn max  │ Toàn bộ số tiền│ Áp dụng trực tiếp        │
│ Ví dụ: 25% max   │ Ví dụ: 50,000  │ shipping_fee = 0         │
│ 500,000 VND      │ VND            │                          │
└──────────────────┴────────────────┴──────────────────────────┘
```

#### **Quy Trình Tính Toán Khuyến Mãi:**

```javascript
/**
 * Quy trình tính toán tổng tiền cuối cùng
 *
 * 1. Tính tổng phụ = Tổng giá trị sản phẩm
 * 2. Kiểm tra mã khuyến mãi có hợp lệ
 * 3. Tính khuyến mãi theo loại
 * 4. Tính phí giao hàng (có thể bị ghi đè bởi FREE_SHIPPING)
 * 5. Tổng tiền cuối cùng = Tổng phụ - Khuyến mãi + Phí giao
 */

function calculateFinalPrice(order) {
  // Bước 1: Tính tổng phụ
  let subtotal = 0;
  order.items.forEach((item) => {
    subtotal += item.price * item.quantity;
  });
  console.log(`📊 Subtotal: ${subtotal.toLocaleString()} VND`);

  // Bước 2: Kiểm tra mã khuyến mãi
  let discount = 0;
  let discountType = null;

  if (order.promoCode) {
    const promo = validatePromoCode(order.promoCode, subtotal);

    if (promo.isValid) {
      discountType = promo.type;

      // Bước 3: Tính khuyến mãi theo loại
      switch (promo.type) {
        case "PERCENTAGE":
          // Tính % và giới hạn bằng maxDiscountAmount
          const percentageDiscount = subtotal * (promo.value / 100);
          discount = Math.min(percentageDiscount, promo.maxDiscountAmount);
          console.log(
            `💵 PERCENTAGE: ${promo.value}% = ${discount.toLocaleString()} VND`
          );
          break;

        case "FIXED_AMOUNT":
          // Giảm số tiền cố định (không vượt quá subtotal)
          discount = Math.min(promo.value, subtotal);
          console.log(`💵 FIXED_AMOUNT: ${discount.toLocaleString()} VND`);
          break;

        case "FREE_SHIPPING":
          // Khuyến mãi này ghi đè phí giao - xử lý sau
          console.log(`🚚 FREE_SHIPPING: Phí giao = 0 VND`);
          break;
      }
    }
  }

  // Bước 4: Tính phí giao hàng
  let shippingFee = calculateShippingFee(
    order.deliveryDistance,
    discountType === "FREE_SHIPPING" // Ghi đè nếu là FREE_SHIPPING
  );
  console.log(`🚚 Shipping Fee: ${shippingFee.toLocaleString()} VND`);

  // Bước 5: Tính tổng tiền cuối cùng
  const finalTotal = subtotal - discount + shippingFee;

  return {
    subtotal,
    discount,
    shippingFee,
    finalTotal,
    breakdown: {
      subtotal: `${subtotal.toLocaleString()} VND`,
      discount: `-${discount.toLocaleString()} VND`,
      shippingFee: `+${shippingFee.toLocaleString()} VND`,
      finalTotal: `${finalTotal.toLocaleString()} VND`,
    },
  };
}
```

#### **Ví Dụ Tính Toán Khuyến Mãi (PERCENTAGE):**

```
📦 ĐƠN HÀNG EXAMPLE:
┌─────────────────────────────────────────┐
│ 2× Cơm Gà Hainaam @ 85,000 VND         │
│ 1× Nước Cam @ 15,000 VND               │
│ SUBTOTAL:                    180,000 VND │
└─────────────────────────────────────────┘

💳 PROMO CODE: "WELCOME25"
   Type: PERCENTAGE
   Value: 25%
   Max Discount: 500,000 VND
   Min Amount: 150,000 VND ✓ (180,000 ≥ 150,000)

🧮 TÍNH KHUYẾN MÃI:
   Discount = 180,000 × (25 / 100) = 45,000 VND
   (45,000 < 500,000, nên áp dụng 45,000)

🚚 PHƯƠNG TIỆN GIAO:
   Distance = 4.5 km
   Shipping = 15,000 + (4.5 - 3) × 5,000 = 22,500 VND

💰 TỔNG TIỀN CUỐI CÙNG:
   Final = 180,000 - 45,000 + 22,500 = 157,500 VND
```

#### **Ví Dụ Tính Toán Khuyến Mãi (FREE_SHIPPING):**

```
📦 ĐƠN HÀNG EXAMPLE:
┌─────────────────────────────────────────┐
│ 3× Phở Bò Tái Nam @ 65,000 VND         │
│ 1× Nước Chanh @ 12,000 VND             │
│ SUBTOTAL:                    207,000 VND │
└─────────────────────────────────────────┘

💳 PROMO CODE: "SHIPPING2024"
   Type: FREE_SHIPPING
   (Tự động miễn phí vận chuyển)

🧮 TÍNH KHUYẾN MÃI:
   Discount = 0 VND (FREE_SHIPPING không giảm giá sản phẩm)

🚚 PHƯƠNG TIỆN GIAO:
   Distance = 8.2 km
   Normal Shipping = 15,000 + (8.2 - 3) × 5,000 = 41,000 VND
   ❌ NHƯNG có FREE_SHIPPING promo
   ✅ Actual Shipping = 0 VND (Giảm 41,000 VND)

💰 TỔNG TIỀN CUỐI CÙNG:
   Final = 207,000 - 0 + 0 = 207,000 VND
   (Tiết kiệm 41,000 VND phí vận chuyển!)
```

#### **Ví Dụ Tính Toán Khuyến Mãi (FIXED_AMOUNT):**

```
📦 ĐƠN HÀNG EXAMPLE:
┌─────────────────────────────────────────┐
│ 2× Cơm Chiên Đương Châu @ 75,000 VND   │
│ 1× Trà Sữa @ 35,000 VND                │
│ SUBTOTAL:                    185,000 VND │
└─────────────────────────────────────────┘

💳 PROMO CODE: "FIXED50K"
   Type: FIXED_AMOUNT
   Value: 50,000 VND (cố định)
   Min Amount: 150,000 VND ✓ (185,000 ≥ 150,000)

🧮 TÍNH KHUYẾN MÃI:
   Discount = min(50,000, 185,000) = 50,000 VND
   (50,000 < 185,000, nên áp dụng toàn bộ)

🚚 PHƯƠNG TIỆN GIAO:
   Distance = 2.8 km
   Shipping = 15,000 VND (0-3 km)

💰 TỔNG TIỀN CUỐI CÙNG:
   Final = 185,000 - 50,000 + 15,000 = 150,000 VND
```

---

### 3️⃣ **Quy Trình Xác Thực Mã Khuyến Mãi**

```
START
  │
  ├─► 1. Kiểm tra code tồn tại trong DB
  │     ❌ Không → Error: "Invalid promo code"
  │     ✓ Có → Tiếp tục
  │
  ├─► 2. Kiểm tra status = ACTIVE
  │     ❌ Inactive → Error: "Promo code is inactive"
  │     ✓ Active → Tiếp tục
  │
  ├─► 3. Kiểm tra ngày hết hạn
  │     ❌ Hết hạn → Error: "Promo code expired"
  │     ✓ Còn hạn → Tiếp tục
  │
  ├─► 4. Kiểm tra giới hạn sử dụng
  │     ❌ Vượt quá → Error: "Usage limit exceeded"
  │     ✓ Còn quota → Tiếp tục
  │
  ├─► 5. Kiểm tra tối thiểu đơn hàng (minAmount)
  │     ❌ Không đủ → Error: "Minimum order amount required"
  │     ✓ Đủ → Tiếp tục
  │
  └─► ✅ VALID - Áp dụng khuyến mãi
       Cập nhật: usedCount++
END
```

---

```
pdq-restaurant/
├── pdq-BE/                          # Spring Boot Backend
│   ├── src/main/java/com/pdq/
│   │   ├── controller/
│   │   │   ├── PromoCodeController.java      # 7 REST endpoints
│   │   │   ├── OrderController.java
│   │   │   └── AuthController.java
│   │   ├── service/
│   │   │   ├── PromoCodeService.java         # 15+ methods for CRUD & validation
│   │   │   ├── OrderService.java             # Haversine shipping calculation
│   │   │   └── AuthService.java              # Auto promo code generation
│   │   ├── entity/
│   │   │   ├── PromoCode.java               # 18 fields, DiscountType enum
│   │   │   └── Order.java                   # promo_code & lat/lng fields
│   │   └── repository/PromoCodeRepository.java
│   ├── src/main/resources/db/migration/
│   │   ├── V8__Create_promo_codes_table.sql
│   │   └── V9__Add_promo_code_to_orders.sql
│   └── pom.xml
│
├── frontend/                        # Vue.js Frontend
│   ├── src/
│   │   ├── admin/
│   │   │   ├── Orders.vue           # Order management, filters, Material Design buttons
│   │   │   ├── PromoCodeManager.vue # Create/Edit/Delete promo codes
│   │   │   └── Dashboard.vue
│   │   ├── pages/
│   │   │   ├── Checkout.vue         # Geo-location, promo code input
│   │   │   ├── Menu.vue
│   │   │   └── Profile.vue
│   │   └── components/
│   └── package.json
│
├── docker/                          # Docker Deployment
│   ├── docker-compose.yml           # 7 services: Backend, Frontend, MySQL, Redis, RabbitMQ, Ngrok
│   ├── .env.example
│   ├── Dockerfile (backend)
│   ├── Dockerfile (frontend)
│   └── nginx.conf
│
└── README.md
```

---

## 🔌 REST API Endpoints

### Promo Code Endpoints (PromoCodeController)

```
POST   /api/promo-codes                 # Create new promo code (Admin)
GET    /api/promo-codes                 # Get all promo codes (Admin)
GET    /api/promo-codes/:id             # Get details
PUT    /api/promo-codes/:id             # Update promo code (Admin)
DELETE /api/promo-codes/:id             # Delete promo code (Admin)
PUT    /api/promo-codes/:id/toggle      # Toggle active status
GET    /api/promo-codes/validate/:code  # Validate code for checkout
```

### Order Integration

```
POST /api/orders                        # Create order with promo code validation
  {
    "items": [...],
    "promoCode": "WELCOME25",           # Optional
    "deliveryLat": 10.7769,
    "deliveryLng": 106.7009,
    ...
  }
```

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
