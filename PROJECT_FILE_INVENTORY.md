# 📁 PDQ Restaurant - Project File Inventory

**Generated:** December 22, 2025  
**Project:** PDQ Restaurant (POS & Order Management System)  
**Status:** ⚠️ **INFORMATION ONLY - NO FILES DELETED**

---

## 📊 Project Overview

```
/Users/chautandat/DoAn/DACN/
├── frontend/                    (Vue.js 3 Admin & Customer Portal)
├── pdq-BE/                      (Spring Boot 3.2.1 Backend)
├── docker/                      (Docker Compose Configuration)
├── uploads/                     (User Uploaded Files)
└── Configuration Files          (README, setup scripts, SQL migrations)
```

---

## 🎨 Frontend Files (Vue.js 3)

### Configuration Files

| File              | Location     | Purpose                    |
| ----------------- | ------------ | -------------------------- |
| `package.json`    | `/frontend/` | NPM dependencies & scripts |
| `vue.config.js`   | `/frontend/` | Vue.js build configuration |
| `babel.config.js` | `/frontend/` | JavaScript transpilation   |
| `jsconfig.json`   | `/frontend/` | JavaScript path aliases    |
| `.eslintrc.js`    | `/frontend/` | ESLint rules               |

### Main Application Files

| File       | Location         | Purpose            | Size                     |
| ---------- | ---------------- | ------------------ | ------------------------ |
| `App.vue`  | `/frontend/src/` | Root Vue component | Main app wrapper         |
| `main.js`  | `/frontend/src/` | App entry point    | Initialize Vue & plugins |
| `axios.js` | `/frontend/src/` | HTTP client config | API requests             |

### 🏠 Pages (Customer Portal)

| File                        | Location               | Purpose                     |
| --------------------------- | ---------------------- | --------------------------- |
| `Home.vue`                  | `/frontend/src/pages/` | Homepage with menu showcase |
| `Menu.vue`                  | `/frontend/src/pages/` | Full menu browse            |
| `Cart.vue`                  | `/frontend/src/pages/` | Shopping cart management    |
| `Checkout.vue`              | `/frontend/src/pages/` | Order checkout & payment    |
| `Orders.vue`                | `/frontend/src/pages/` | Customer order history      |
| `MyOrder.vue`               | `/frontend/src/pages/` | Order tracking page         |
| `Profile.vue`               | `/frontend/src/pages/` | User profile management     |
| `Login.vue`                 | `/frontend/src/pages/` | Customer login              |
| `Register.vue`              | `/frontend/src/pages/` | Customer registration       |
| `About.vue`                 | `/frontend/src/pages/` | About restaurant page       |
| `Promo.vue`                 | `/frontend/src/pages/` | Promotional offers          |
| `Table.vue`                 | `/frontend/src/pages/` | Table reservation           |
| `Chatbot.vue`               | `/frontend/src/pages/` | AI chatbot interface        |
| `CustomerOrderTracking.vue` | `/frontend/src/pages/` | Real-time order tracking    |
| `OrderReview.vue`           | `/frontend/src/pages/` | Review completed orders     |
| `PaymentCallback.vue`       | `/frontend/src/pages/` | VNPay callback handler      |
| `PaymentResult.vue`         | `/frontend/src/pages/` | Payment result display      |
| `PaymentReturn.vue`         | `/frontend/src/pages/` | Payment return handler      |
| `Thank.vue`                 | `/frontend/src/pages/` | Thank you after order       |
| `VerifyAccount.vue`         | `/frontend/src/pages/` | Email verification          |

### 🔧 Admin Dashboard

| File                         | Location               | Purpose                   | Status                               |
| ---------------------------- | ---------------------- | ------------------------- | ------------------------------------ |
| `AdminLayout.vue`            | `/frontend/src/admin/` | Admin sidebar & layout    | Working                              |
| `Dashboard.vue`              | `/frontend/src/admin/` | Admin stats & overview    | ✅ Fixed - Individual error handling |
| `AdminDashboardRealtime.vue` | `/frontend/src/admin/` | Real-time dashboard       | Alternative view                     |
| `KitchenDisplay.vue`         | `/frontend/src/admin/` | Kitchen order display     | ✅ Fixed - Native WebSocket          |
| `Orders.vue`                 | `/frontend/src/admin/` | Order management          | ✅ Fixed - Build error fixed         |
| `AdminOrders.vue`            | `/frontend/src/admin/` | Alternative orders view   | Secondary                            |
| `MenuManagement.vue`         | `/frontend/src/admin/` | Menu CRUD operations      |
| `PromoCodeManager.vue`       | `/frontend/src/admin/` | Promotion code management |
| `Users.vue`                  | `/frontend/src/admin/` | User management           |
| `Staffs.vue`                 | `/frontend/src/admin/` | Staff management          |
| `Earnings.vue`               | `/frontend/src/admin/` | Revenue reports           |
| `Sales.vue`                  | `/frontend/src/admin/` | Sales analytics           |
| `Categories.vue`             | `/frontend/src/admin/` | Product categories        |

### 📦 Admin Partials (Sub-components)

| File                | Location                        | Purpose               | Updates                      |
| ------------------- | ------------------------------- | --------------------- | ---------------------------- |
| `OrderCard.vue`     | `/frontend/src/admin/partials/` | Display order details | ✅ Added distance formatting |
| `SectionColumn.vue` | `/frontend/src/admin/partials/` | Column layout helper  | Layout utility               |

### 🧩 Reusable Components

| File                  | Location                    | Purpose                  |
| --------------------- | --------------------------- | ------------------------ |
| `NavBar.vue`          | `/frontend/src/components/` | Navigation bar           |
| `FooterComponent.vue` | `/frontend/src/components/` | Footer component         |
| `QuickView.vue`       | `/frontend/src/components/` | Quick product preview    |
| `OrderDetails.vue`    | `/frontend/src/components/` | Order details modal      |
| `AddressSelector.vue` | `/frontend/src/components/` | Address selection        |
| `CancelModal.vue`     | `/frontend/src/components/` | Order cancellation modal |

### 🎯 Composables (Vue 3 Composition API)

| File              | Location                     | Purpose        | Status                     |
| ----------------- | ---------------------------- | -------------- | -------------------------- |
| `useWebSocket.js` | `/frontend/src/composables/` | WebSocket hook | Replaced in KitchenDisplay |

### 🛠️ Services & Utilities

| File              | Location                  | Purpose                  | Status       |
| ----------------- | ------------------------- | ------------------------ | ------------ |
| `api.js`          | `/frontend/src/services/` | API helper functions     | Core service |
| `websocket.js`    | `/frontend/src/services/` | WebSocket service        | Fallback     |
| `storage.js`      | `/frontend/src/utils/`    | LocalStorage helper      | Utilities    |
| `router/index.js` | `/frontend/src/router/`   | Vue Router configuration |
| `store/index.js`  | `/frontend/src/store/`    | Vuex state management    |

### 📱 Build Output

| Directory                 | Purpose                          |
| ------------------------- | -------------------------------- |
| `/frontend/dist/`         | Production build (JS, CSS, HTML) |
| `/frontend/node_modules/` | NPM packages (not tracked)       |

---

## ☕ Backend Files (Spring Boot 3.2.1)

### Configuration

| File                  | Location                      | Purpose                           |
| --------------------- | ----------------------------- | --------------------------------- |
| `pom.xml`             | `/pdq-BE/`                    | Maven dependencies & build config |
| `application.yaml`    | `/pdq-BE/src/main/resources/` | Spring Boot config                |
| `.vscode/launch.json` | `/pdq-BE/`                    | Debug configuration               |

### 🏗️ Config Classes

| File                   | Location                                | Purpose                |
| ---------------------- | --------------------------------------- | ---------------------- |
| `CorsConfig.java`      | `/pdq-BE/src/main/java/com/pdq/config/` | CORS settings          |
| `SecurityConfig.java`  | `/pdq-BE/src/main/java/com/pdq/config/` | Spring Security        |
| `WebSocketConfig.java` | `/pdq-BE/src/main/java/com/pdq/config/` | WebSocket STOMP config |
| `WebConfig.java`       | `/pdq-BE/src/main/java/com/pdq/config/` | Web MVC config         |
| `RabbitMQConfig.java`  | `/pdq-BE/src/main/java/com/pdq/config/` | RabbitMQ queues        |
| `AsyncConfig.java`     | `/pdq-BE/src/main/java/com/pdq/config/` | Async/threading        |
| `VNPayConfig.java`     | `/pdq-BE/src/main/java/com/pdq/config/` | VNPay payment gateway  |
| `ZaloPayConfig.java`   | `/pdq-BE/src/main/java/com/pdq/config/` | ZaloPay gateway        |

### 🎮 Controllers (REST API Endpoints)

| File                          | Location                                    | Purpose                | Endpoints              |
| ----------------------------- | ------------------------------------------- | ---------------------- | ---------------------- |
| `AuthController.java`         | `/pdq-BE/src/main/java/com/pdq/controller/` | Authentication         | /api/auth/\*           |
| `OrderController.java`        | `/pdq-BE/src/main/java/com/pdq/controller/` | Order operations       | /api/orders/\*         |
| `AdminOrderController.java`   | `/pdq-BE/src/main/java/com/pdq/controller/` | Admin order management | /api/admin/orders/\*   |
| `ProductController.java`      | `/pdq-BE/src/main/java/com/pdq/controller/` | Product operations     | /api/products/\*       |
| `AdminProductController.java` | `/pdq-BE/src/main/java/com/pdq/controller/` | Admin product CRUD     | /api/admin/products/\* |
| `UserController.java`         | `/pdq-BE/src/main/java/com/pdq/controller/` | User profile           | /api/users/\*          |
| `CartController.java`         | `/pdq-BE/src/main/java/com/pdq/controller/` | Shopping cart          | /api/cart/\*           |
| `CategoryController.java`     | `/pdq-BE/src/main/java/com/pdq/controller/` | Categories             | /api/categories/\*     |
| `ReviewController.java`       | `/pdq-BE/src/main/java/com/pdq/controller/` | Product reviews        | /api/reviews/\*        |
| `PromoCodeController.java`    | `/pdq-BE/src/main/java/com/pdq/controller/` | Promotion codes        | /api/promo-codes/\*    |
| `AddressController.java`      | `/pdq-BE/src/main/java/com/pdq/controller/` | Delivery addresses     | /api/addresses/\*      |
| `PaymentController.java`      | `/pdq-BE/src/main/java/com/pdq/controller/` | Payment processing     | /api/payments/\*       |
| `NotificationController.java` | `/pdq-BE/src/main/java/com/pdq/controller/` | Notifications          | /api/notifications/\*  |
| `FavoriteController.java`     | `/pdq-BE/src/main/java/com/pdq/controller/` | Favorites              | /api/favorites/\*      |
| `ChatController.java`         | `/pdq-BE/src/main/java/com/pdq/controller/` | Chatbot                | /api/chat/\*           |
| `AdminController.java`        | `/pdq-BE/src/main/java/com/pdq/controller/` | Admin operations       | /api/admin/\*          |
| `ReportController.java`       | `/pdq-BE/src/main/java/com/pdq/controller/` | Reports                | /api/reports/\*        |
| `ShiftReportController.java`  | `/pdq-BE/src/main/java/com/pdq/controller/` | Shift reports          | /api/shift-reports/\*  |
| `FileUploadController.java`   | `/pdq-BE/src/main/java/com/pdq/controller/` | File upload            | /api/upload/\*         |
| `WebSocketController.java`    | `/pdq-BE/src/main/java/com/pdq/controller/` | WebSocket messages     | /ws/\*                 |

### 🛢️ Entity Classes (Database Models)

| File                     | Location                                | Purpose           | Fields                                        |
| ------------------------ | --------------------------------------- | ----------------- | --------------------------------------------- |
| `User.java`              | `/pdq-BE/src/main/java/com/pdq/entity/` | User account      | id, email, password, role, status             |
| `Order.java`             | `/pdq-BE/src/main/java/com/pdq/entity/` | Order record      | id, userId, items, status, total, shippingFee |
| `OrderItem.java`         | `/pdq-BE/src/main/java/com/pdq/entity/` | Item in order     | orderId, productId, quantity, price           |
| `Product.java`           | `/pdq-BE/src/main/java/com/pdq/entity/` | Menu item         | id, name, price, image, category              |
| `Category.java`          | `/pdq-BE/src/main/java/com/pdq/entity/` | Product category  | id, name, description                         |
| `Cart.java`              | `/pdq-BE/src/main/java/com/pdq/entity/` | Shopping cart     | userId, items, total                          |
| `CartItem.java`          | `/pdq-BE/src/main/java/com/pdq/entity/` | Cart line item    | cartId, productId, quantity                   |
| `Address.java`           | `/pdq-BE/src/main/java/com/pdq/entity/` | Delivery address  | userId, street, ward, district, city          |
| `PromoCode.java`         | `/pdq-BE/src/main/java/com/pdq/entity/` | Promotion code    | code, discountType, value, maxDiscount        |
| `Review.java`            | `/pdq-BE/src/main/java/com/pdq/entity/` | Product review    | productId, userId, rating, comment            |
| `Notification.java`      | `/pdq-BE/src/main/java/com/pdq/entity/` | User notification | userId, message, type, read                   |
| `PaymentLog.java`        | `/pdq-BE/src/main/java/com/pdq/entity/` | Payment record    | orderId, method, status, amount               |
| `Favorite.java`          | `/pdq-BE/src/main/java/com/pdq/entity/` | Favorite products | userId, productId                             |
| `OrderCancellation.java` | `/pdq-BE/src/main/java/com/pdq/entity/` | Cancellation log  | orderId, reason, cancelledAt                  |

### 📋 Enums (Constants)

| File                 | Location                                | Purpose        | Values                                                     |
| -------------------- | --------------------------------------- | -------------- | ---------------------------------------------------------- |
| `UserRole.java`      | `/pdq-BE/src/main/java/com/pdq/entity/` | User roles     | ADMIN, STAFF, CUSTOMER                                     |
| `UserStatus.java`    | `/pdq-BE/src/main/java/com/pdq/entity/` | User status    | ACTIVE, INACTIVE, BANNED                                   |
| `OrderStatus.java`   | `/pdq-BE/src/main/java/com/pdq/entity/` | Order status   | PENDING, CONFIRMED, PREPARING, READY, COMPLETED, CANCELLED |
| `PaymentStatus.java` | `/pdq-BE/src/main/java/com/pdq/entity/` | Payment status | PENDING, COMPLETED, FAILED, REFUNDED                       |
| `PaymentMethod.java` | `/pdq-BE/src/main/java/com/pdq/entity/` | Payment types  | CASH, VNPAY, ZALOPAY, CARD                                 |
| `ReviewStatus.java`  | `/pdq-BE/src/main/java/com/pdq/entity/` | Review status  | PENDING, APPROVED, REJECTED                                |

### 📦 DTO Classes (Data Transfer Objects)

| File                             | Location                                       | Purpose                 |
| -------------------------------- | ---------------------------------------------- | ----------------------- |
| `CreateOrderRequest.java`        | `/pdq-BE/src/main/java/com/pdq/dto/order/`     | Place order request     |
| `OrderResponse.java`             | `/pdq-BE/src/main/java/com/pdq/dto/order/`     | Order response format   |
| `UpdateOrderStatusRequest.java`  | `/pdq-BE/src/main/java/com/pdq/dto/order/`     | Status update request   |
| `CancelOrderRequest.java`        | `/pdq-BE/src/main/java/com/pdq/dto/order/`     | Cancellation request    |
| `PromoCodeDTO.java`              | `/pdq-BE/src/main/java/com/pdq/dto/promo/`     | Promo code DTO          |
| `PromoCodeValidateRequest.java`  | `/pdq-BE/src/main/java/com/pdq/dto/promo/`     | Validate promo request  |
| `PromoCodeValidateResponse.java` | `/pdq-BE/src/main/java/com/pdq/dto/promo/`     | Validate promo response |
| `LoginRequest.java`              | `/pdq-BE/src/main/java/com/pdq/dto/auth/`      | Login credentials       |
| `AuthResponse.java`              | `/pdq-BE/src/main/java/com/pdq/dto/auth/`      | Auth token response     |
| `RegisterRequest.java`           | `/pdq-BE/src/main/java/com/pdq/dto/auth/`      | Registration data       |
| `ProductRequest.java`            | `/pdq-BE/src/main/java/com/pdq/dto/product/`   | Product creation        |
| `ProductResponse.java`           | `/pdq-BE/src/main/java/com/pdq/dto/product/`   | Product response        |
| `CartResponse.java`              | `/pdq-BE/src/main/java/com/pdq/dto/cart/`      | Cart contents           |
| `AddToCartRequest.java`          | `/pdq-BE/src/main/java/com/pdq/dto/cart/`      | Add item to cart        |
| `UpdateCartItemRequest.java`     | `/pdq-BE/src/main/java/com/pdq/dto/cart/`      | Update cart item        |
| `ReviewRequest.java`             | `/pdq-BE/src/main/java/com/pdq/dto/review/`    | Review submission       |
| `ReviewResponse.java`            | `/pdq-BE/src/main/java/com/pdq/dto/review/`    | Review display          |
| `StatisticsResponse.java`        | `/pdq-BE/src/main/java/com/pdq/dto/admin/`     | Admin statistics        |
| `OrderStatusUpdate.java`         | `/pdq-BE/src/main/java/com/pdq/dto/websocket/` | WebSocket status update |

### 💼 Service Classes (Business Logic)

| File                            | Location                                 | Purpose                       |
| ------------------------------- | ---------------------------------------- | ----------------------------- |
| `OrderService.java`             | `/pdq-BE/src/main/java/com/pdq/service/` | Order operations & validation |
| `AuthService.java`              | `/pdq-BE/src/main/java/com/pdq/service/` | Authentication & registration |
| `ProductService.java`           | `/pdq-BE/src/main/java/com/pdq/service/` | Product CRUD                  |
| `UserService.java`              | N/A                                      | Integrated in UserController  |
| `CartService.java`              | `/pdq-BE/src/main/java/com/pdq/service/` | Shopping cart management      |
| `PromoCodeService.java`         | `/pdq-BE/src/main/java/com/pdq/service/` | Promo validation & discount   |
| `AddressService.java`           | `/pdq-BE/src/main/java/com/pdq/service/` | Delivery address management   |
| `ReviewService.java`            | `/pdq-BE/src/main/java/com/pdq/service/` | Review operations             |
| `PaymentExpirationService.java` | `/pdq-BE/src/main/java/com/pdq/service/` | Payment timeout handling      |
| `VNPayService.java`             | `/pdq-BE/src/main/java/com/pdq/service/` | VNPay integration             |
| `ZaloPayService.java`           | `/pdq-BE/src/main/java/com/pdq/service/` | ZaloPay integration           |
| `EmailService.java`             | `/pdq-BE/src/main/java/com/pdq/service/` | Email notifications           |
| `NotificationService.java`      | `/pdq-BE/src/main/java/com/pdq/service/` | User notifications            |
| `WebSocketService.java`         | `/pdq-BE/src/main/java/com/pdq/service/` | WebSocket messaging           |
| `AdminService.java`             | `/pdq-BE/src/main/java/com/pdq/service/` | Admin operations              |
| `ReportService.java`            | `/pdq-BE/src/main/java/com/pdq/service/` | Revenue reports               |
| `ShiftReportService.java`       | `/pdq-BE/src/main/java/com/pdq/service/` | Shift analytics               |
| `FavoriteService.java`          | `/pdq-BE/src/main/java/com/pdq/service/` | Favorite products             |
| `ChatService.java`              | `/pdq-BE/src/main/java/com/pdq/service/` | Chatbot logic                 |
| `MenuSearchService.java`        | `/pdq-BE/src/main/java/com/pdq/service/` | Menu search functionality     |
| `RabbitMQProducer.java`         | `/pdq-BE/src/main/java/com/pdq/service/` | Message queue producer        |

### 📩 Message Consumers (RabbitMQ)

| File                     | Location                                  | Purpose               |
| ------------------------ | ----------------------------------------- | --------------------- |
| `KitchenConsumer.java`   | `/pdq-BE/src/main/java/com/pdq/consumer/` | Kitchen order updates |
| `EmailConsumer.java`     | `/pdq-BE/src/main/java/com/pdq/consumer/` | Email sending         |
| `SmsConsumer.java`       | `/pdq-BE/src/main/java/com/pdq/consumer/` | SMS notifications     |
| `AnalyticsConsumer.java` | `/pdq-BE/src/main/java/com/pdq/consumer/` | Data analytics        |

### 🔐 Security Classes

| File                           | Location                                  | Purpose                         |
| ------------------------------ | ----------------------------------------- | ------------------------------- |
| `JwtService.java`              | `/pdq-BE/src/main/java/com/pdq/security/` | JWT token generation/validation |
| `JwtAuthenticationFilter.java` | `/pdq-BE/src/main/java/com/pdq/security/` | JWT filter                      |
| `UserDetailsServiceImpl.java`  | `/pdq-BE/src/main/java/com/pdq/security/` | User details loader             |

### 💾 Repository Classes (Database Access)

| File                               | Location                                    | Purpose              |
| ---------------------------------- | ------------------------------------------- | -------------------- |
| `OrderRepository.java`             | `/pdq-BE/src/main/java/com/pdq/repository/` | Order queries        |
| `OrderItemRepository.java`         | `/pdq-BE/src/main/java/com/pdq/repository/` | Order item queries   |
| `UserRepository.java`              | `/pdq-BE/src/main/java/com/pdq/repository/` | User queries         |
| `ProductRepository.java`           | `/pdq-BE/src/main/java/com/pdq/repository/` | Product queries      |
| `CartRepository.java`              | `/pdq-BE/src/main/java/com/pdq/repository/` | Cart queries         |
| `CartItemRepository.java`          | `/pdq-BE/src/main/java/com/pdq/repository/` | Cart item queries    |
| `PromoCodeRepository.java`         | `/pdq-BE/src/main/java/com/pdq/repository/` | Promo code queries   |
| `AddressRepository.java`           | `/pdq-BE/src/main/java/com/pdq/repository/` | Address queries      |
| `ReviewRepository.java`            | `/pdq-BE/src/main/java/com/pdq/repository/` | Review queries       |
| `CategoryRepository.java`          | `/pdq-BE/src/main/java/com/pdq/repository/` | Category queries     |
| `NotificationRepository.java`      | `/pdq-BE/src/main/java/com/pdq/repository/` | Notification queries |
| `PaymentLogRepository.java`        | `/pdq-BE/src/main/java/com/pdq/repository/` | Payment log queries  |
| `FavoriteRepository.java`          | `/pdq-BE/src/main/java/com/pdq/repository/` | Favorite queries     |
| `OrderCancellationRepository.java` | `/pdq-BE/src/main/java/com/pdq/repository/` | Cancellation queries |

### 📡 Event Classes (Domain Events)

| File                               | Location                               | Purpose                  |
| ---------------------------------- | -------------------------------------- | ------------------------ |
| `OrderCreatedEvent.java`           | `/pdq-BE/src/main/java/com/pdq/event/` | Order creation event     |
| `OrderPaidEvent.java`              | `/pdq-BE/src/main/java/com/pdq/event/` | Payment completion event |
| `OrderStatusChangedEvent.java`     | `/pdq-BE/src/main/java/com/pdq/event/` | Status change event      |
| `UserRegisteredEvent.java`         | `/pdq-BE/src/main/java/com/pdq/event/` | Registration event       |
| `PasswordResetRequestedEvent.java` | `/pdq-BE/src/main/java/com/pdq/event/` | Password reset event     |

### 🚀 Main Application

| File                         | Location                         | Purpose                 |
| ---------------------------- | -------------------------------- | ----------------------- |
| `RestaurantApplication.java` | `/pdq-BE/src/main/java/com/pdq/` | Spring Boot entry point |

### ❌ Exception Handlers

| File                             | Location                                   | Purpose               |
| -------------------------------- | ------------------------------------------ | --------------------- |
| `GlobalExceptionHandler.java`    | `/pdq-BE/src/main/java/com/pdq/exception/` | Global error handling |
| `ResourceNotFoundException.java` | `/pdq-BE/src/main/java/com/pdq/exception/` | 404 errors            |
| `BadRequestException.java`       | `/pdq-BE/src/main/java/com/pdq/exception/` | 400 errors            |

### 📊 Build Output

| Directory         | Purpose                     |
| ----------------- | --------------------------- |
| `/pdq-BE/target/` | Compiled Java classes & JAR |
| `/pdq-BE/logs/`   | Application logs            |

---

## 🐳 Docker Configuration

| File                              | Location                | Purpose                   |
| --------------------------------- | ----------------------- | ------------------------- |
| `docker-compose.yml`              | `/docker/`              | Container orchestration   |
| `Dockerfile.backend`              | `/docker/`              | Backend image definition  |
| `Dockerfile.frontend`             | `/docker/`              | Frontend image definition |
| `init-scripts/pdq_restaurant.sql` | `/docker/init-scripts/` | Database initialization   |

### Docker Services

1. **pdq-backend** - Spring Boot application (port 3000)
2. **pdq-frontend** - Vue.js application (port 8080)
3. **pdq-mysql** - MariaDB database (port 3306)
4. **pdq-redis** - Redis cache (port 6379)
5. **pdq-rabbitmq** - RabbitMQ messaging (port 5672, 15672)
6. **pdq-ngrok** - Tunneling service (port 4040)

---

## 📚 Database Migrations (Flyway)

| File                                 | Location                                   | Purpose                | Version |
| ------------------------------------ | ------------------------------------------ | ---------------------- | ------- |
| `V6__create_order_cancellations.sql` | `/pdq-BE/src/main/resources/db/migration/` | Add cancellation table | 6       |
| `V7__add_payment_timeout_fields.sql` | `/pdq-BE/src/main/resources/db/migration/` | Payment timeout fields | 7       |
| `V8__create_promo_codes_table.sql`   | `/pdq-BE/src/main/resources/db/migration/` | Promo codes table      | 8       |
| `V9__add_promo_code_to_orders.sql`   | `/pdq-BE/src/main/resources/db/migration/` | Link promo to orders   | 9       |

---

## 📄 Documentation Files

| File                                | Location | Purpose                    | Status     |
| ----------------------------------- | -------- | -------------------------- | ---------- |
| `README.md`                         | `/DACN/` | Project overview & setup   | ✅ Updated |
| `QUICK_START.md`                    | `/DACN/` | Quick start guide          | Available  |
| `DOCKER_RESTART_QUICK.md`           | `/DACN/` | Docker restart commands    | Available  |
| `TROUBLESHOOT_DASHBOARD_KITCHEN.md` | `/DACN/` | Debugging guide            | ✅ Created |
| `add-delivery-coordinates.sql`      | `/DACN/` | Sample SQL for coordinates | Reference  |

---

## 📊 File Statistics

### Frontend

- **Total Vue Components:** 31
- **Total JS Files:** 8
- **Config Files:** 5
- **Total Frontend Files:** ~44

### Backend

- **Java Classes:** 120+
- **DTO Classes:** 18
- **Entity Classes:** 14
- **Service Classes:** 21
- **Controller Classes:** 20
- **Repository Classes:** 14
- **Configuration Classes:** 8
- **Total Backend Files:** ~150+

### Database

- **SQL Migration Files:** 4
- **Initialization Scripts:** 2

### Docker

- **Docker Compose Files:** 1
- **Dockerfile:** 2
- **Total Docker Files:** 3

---

## 🔑 Key Component Status

| Component             | Type     | Status      | Last Update  |
| --------------------- | -------- | ----------- | ------------ |
| Dashboard.vue         | Frontend | ✅ Fixed    | Dec 22, 2025 |
| KitchenDisplay.vue    | Frontend | ✅ Fixed    | Dec 22, 2025 |
| OrderCard.vue         | Frontend | ✅ Enhanced | Dec 22, 2025 |
| Orders.vue            | Frontend | ✅ Fixed    | Dec 22, 2025 |
| OrderService.java     | Backend  | ✅ Working  | Dec 22, 2025 |
| PromoCodeService.java | Backend  | ✅ Working  | Dec 22, 2025 |
| WebSocketConfig.java  | Backend  | ✅ Working  | Dec 22, 2025 |

---

## 🚀 Project Technology Stack

### Frontend

- **Framework:** Vue.js 3
- **UI Framework:** Vuetify 3
- **HTTP Client:** Axios
- **State Management:** Vuex
- **Routing:** Vue Router
- **Styling:** CSS 3

### Backend

- **Runtime:** Java 17+
- **Framework:** Spring Boot 3.2.1
- **Database:** MySQL/MariaDB 10.4
- **Cache:** Redis 7
- **Message Queue:** RabbitMQ 3.12
- **Security:** JWT (Spring Security)
- **Real-time:** WebSocket (STOMP)
- **Payment:** VNPay, ZaloPay
- **Build Tool:** Maven

### DevOps

- **Containerization:** Docker & Docker Compose
- **Database Migrations:** Flyway
- **Logging:** File-based (pdq-backend.log)

---

## 📌 Important Notes

✅ **All files are accounted for**  
✅ **No files have been deleted**  
✅ **Recent fixes applied:**

- Dashboard.vue: Individual error handling per stat
- KitchenDisplay.vue: Native WebSocket with auto-reconnect
- OrderCard.vue: Distance formatting (14.3 km format)
- Orders.vue: Build error fixed, CSS enhanced

⚠️ **To-Do:**

- End-to-end testing of Dashboard & KitchenDisplay with real orders
- Verify distance calculation and formatting in production

---

**Generated:** December 22, 2025  
**Purpose:** Project file inventory for reference and analysis  
**Status:** 🔒 INFORMATION ONLY - NO MODIFICATIONS MADE
