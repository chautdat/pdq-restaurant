package com.pdq.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.beans.factory.annotation.Value;

import com.pdq.dto.common.PageResponse;
import com.pdq.dto.order.CancelOrderRequest;
import com.pdq.dto.order.CreateOrderRequest;
import com.pdq.dto.order.OrderResponse;
import com.pdq.dto.order.UpdateOrderStatusRequest;
import com.pdq.entity.Cart;
import com.pdq.entity.CartItem;
import com.pdq.entity.Order;
import com.pdq.entity.OrderCancellation;
import com.pdq.entity.OrderItem;
import com.pdq.entity.OrderStatus;
import com.pdq.dto.message.OrderMessage;
import com.pdq.entity.PaymentLog;
import com.pdq.entity.PaymentMethod;
import com.pdq.entity.PaymentStatus;
import com.pdq.entity.Product;
import com.pdq.entity.User;
import com.pdq.entity.UserRole;
import com.pdq.exception.BadRequestException;
import com.pdq.exception.ResourceNotFoundException;
import com.pdq.repository.CartItemRepository;
import com.pdq.repository.CartRepository;
import com.pdq.repository.OrderCancellationRepository;
import com.pdq.repository.OrderItemRepository;
import com.pdq.repository.OrderRepository;
import com.pdq.repository.PaymentLogRepository;
import com.pdq.repository.ProductRepository;
import com.pdq.repository.UserRepository;
import com.pdq.service.RabbitMQProducer;
import com.pdq.service.OrderEmailService;
import com.pdq.event.OrderCreatedEvent;
import com.pdq.event.OrderStatusChangedEvent;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OrderCancellationRepository cancellationRepository;
    private final PaymentLogRepository paymentLogRepository;
    private final VNPayService vnPayService;
    private final ZaloPayService zaloPayService; // ✅ THÊM
    private final OrderEmailService orderEmailService;
    private final WebSocketService webSocketService;
    private final ApplicationEventPublisher eventPublisher;
    private final RabbitMQProducer rabbitMQProducer;
    private final PromoCodeService promoCodeService;

    private static final double RESTAURANT_LAT = 10.855232;
    private static final double RESTAURANT_LNG = 106.785780;

    private static final BigDecimal DELIVERY_FEE = new BigDecimal("15000");

    public OrderService(OrderRepository orderRepository,
                       OrderItemRepository orderItemRepository,
                       UserRepository userRepository,
                       CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductRepository productRepository,
                       OrderCancellationRepository cancellationRepository,
                       PaymentLogRepository paymentLogRepository,
                       VNPayService vnPayService,
                       ZaloPayService zaloPayService, // ✅ THÊM
                       OrderEmailService orderEmailService,
                       WebSocketService webSocketService,
                       ApplicationEventPublisher eventPublisher,
                       RabbitMQProducer rabbitMQProducer,
                       PromoCodeService promoCodeService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.cancellationRepository = cancellationRepository;
        this.paymentLogRepository = paymentLogRepository;
        this.vnPayService = vnPayService;
        this.zaloPayService = zaloPayService; // ✅ THÊM
        this.orderEmailService = orderEmailService;
        this.webSocketService = webSocketService;
        this.eventPublisher = eventPublisher;
        this.rabbitMQProducer = rabbitMQProducer;
        this.promoCodeService = promoCodeService;
    }

    @Transactional
    public Map<String, Object> createOrder(String userEmail, CreateOrderRequest request, String ipAddress) {
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("📝 CREATE ORDER - START");
        System.out.println("═══════════════════════════════════════════════════════");
        
        try {
            // 1. Find user
            System.out.println("1️⃣ Looking up user: " + userEmail);
            User user = userRepository.findByEmail(userEmail)
                    .orElseGet(() -> {
                        System.out.println("   → Email not found, trying username...");
                        return userRepository.findByUsername(userEmail)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                    });
            
            System.out.println("   ✅ User found: " + user.getFullName() + " (ID: " + user.getUserId() + ")");

            // 2. Get cart
            System.out.println("2️⃣ Getting cart...");
            Cart cart = cartRepository.findByUser(user)
                    .orElseThrow(() -> new BadRequestException("Cart is empty"));

            if (cart.getItems() == null || cart.getItems().isEmpty()) {
                throw new BadRequestException("Cart is empty");
            }

            System.out.println("   ✅ Cart found with " + cart.getItems().size() + " items");

            // 3. Validate stock
            System.out.println("3️⃣ Validating stock...");
            for (CartItem cartItem : cart.getItems()) {
                Product product = cartItem.getProduct();
                System.out.println("   → " + product.getProductName() + 
                                 ": need " + cartItem.getQuantity() + 
                                 ", available " + product.getStockQuantity());
                
                if (product.getStockQuantity() < cartItem.getQuantity()) {
                    throw new BadRequestException("Product " + product.getProductName() + " is out of stock");
                }
            }
            System.out.println("   ✅ Stock validated");

            // 4. Calculate amounts
            System.out.println("4️⃣ Calculating amounts...");
            
            BigDecimal subtotal = BigDecimal.ZERO;
            for (CartItem item : cart.getItems()) {
                BigDecimal itemSubtotal = item.getSubtotal();
                if (itemSubtotal == null || itemSubtotal.compareTo(BigDecimal.ZERO) == 0) {
                    BigDecimal unitPrice = item.getDiscountPrice() != null
                            && item.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0
                            ? item.getDiscountPrice()
                            : item.getPrice();
                    itemSubtotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                }
                subtotal = subtotal.add(itemSubtotal);
                System.out.println("   → Item: " + item.getProduct().getProductName() +
                        " subtotal: " + itemSubtotal +
                        " (price=" + item.getPrice() + ", discountPrice=" + item.getDiscountPrice() +
                        ", qty=" + item.getQuantity() + ")");
            }
            
            BigDecimal discountAmount = BigDecimal.ZERO;
            BigDecimal shippingFee = DELIVERY_FEE; // Default, will be calculated based on distance
            BigDecimal finalAmount = subtotal.subtract(discountAmount).add(shippingFee);
            BigDecimal totalAmount = finalAmount;
            
            System.out.println("   → Subtotal: " + subtotal);
            System.out.println("   → Discount: " + discountAmount);
            System.out.println("   → Shipping (initial): " + shippingFee);
            System.out.println("   → Total: " + totalAmount);
            System.out.println("   → Final: " + finalAmount);

            // 5. Create order
            System.out.println("5️⃣ Creating order object...");
            Order order = new Order();
            
            String orderNumber = generateOrderNumber();
            order.setOrderNumber(orderNumber);
            System.out.println("   → Order number: " + orderNumber);
            
            order.setUser(user);
            order.setRecipientName(request.getRecipientName());
            order.setPhone(request.getPhone());
            
            String address = request.getAddressLine();
            if (address == null || address.trim().isEmpty()) {
                throw new BadRequestException("Delivery address is required");
            }
            
            order.setDeliveryAddress(address);
            order.setAddressLine(address.length() > 255 ? address.substring(0, 255) : address);
            System.out.println("   → Address: " + address);

            // Geo coords (REQUIRED for shipping fee calculation)
            Double deliveryLat = request.getDeliveryLat();
            Double deliveryLng = request.getDeliveryLng();
            
            if (deliveryLat == null || deliveryLng == null) {
                throw new BadRequestException("Delivery latitude and longitude are required for shipping fee calculation");
            }
            
            order.setDeliveryLat(deliveryLat);
            order.setDeliveryLng(deliveryLng);
            
            order.setSubtotal(subtotal);
            order.setDiscountAmount(discountAmount);
            order.setShippingFee(shippingFee);
            order.setTotalAmount(totalAmount);
            order.setFinalAmount(finalAmount);

            // 4a. Apply promo code if provided (BEFORE calculating delivery info)
            String appliedPromoCode = null;
            if (request.getPromoCode() != null && !request.getPromoCode().trim().isEmpty()) {
                System.out.println("4a️⃣ Applying promo code: " + request.getPromoCode());
                com.pdq.entity.PromoCode promoCode = promoCodeService.validateAndApplyPromoCode(
                    request.getPromoCode().trim(),
                    subtotal,
                    user.getUserId()
                );

                if (promoCode != null) {
                    discountAmount = promoCodeService.calculateDiscountAmount(promoCode, subtotal);
                    appliedPromoCode = promoCode.getCode();

                    // If promo is FREE_SHIPPING, set shippingFee to 0
                    if (com.pdq.entity.PromoCode.DiscountType.FREE_SHIPPING.equals(promoCode.getDiscountType())) {
                        shippingFee = BigDecimal.ZERO;
                    }

                    System.out.println("   ✅ Promo applied: " + appliedPromoCode);
                    System.out.println("   → Discount amount: " + discountAmount);
                    System.out.println("   → Shipping fee after promo: " + shippingFee);
                }
            }

            order.setPromoCode(appliedPromoCode); // 🎁 Set applied promo code

            // Tính lại distance/fee nếu có lat/lng
            calculateDeliveryInfo(order);
            shippingFee = order.getShippingFee();
            finalAmount = order.getFinalAmount();
            totalAmount = order.getTotalAmount();
            
            // ✅ Set payment method (THÊM ZALOPAY)
            String paymentMethodStr = request.getPaymentMethod();
            System.out.println("   → Payment method request: " + paymentMethodStr);
            
            PaymentMethod paymentMethod = PaymentMethod.cash; // Default
            if (paymentMethodStr != null) {
                String normalized = paymentMethodStr.toUpperCase().trim();
                if ("VNPAY".equals(normalized)) {
                    paymentMethod = PaymentMethod.vnpay;
                } else if ("ZALOPAY".equals(normalized)) {
                    paymentMethod = PaymentMethod.zalopay;
                } else if ("CASH".equals(normalized)) {
                    paymentMethod = PaymentMethod.cash;
                }
            }
            
            order.setPaymentMethod(paymentMethod);
            order.setPaymentStatus(PaymentStatus.pending);
            order.setOrderStatus(OrderStatus.pending);
            
            System.out.println("   → Payment method: " + paymentMethod);

            // 6. Save order
            System.out.println("6️⃣ Saving order...");
            order = orderRepository.save(order);
            System.out.println("   ✅ Order saved with ID: " + order.getOrderId());

            // ✅ Set payment expiration and log initial attempt for online payments
            if (paymentMethod == PaymentMethod.vnpay || paymentMethod == PaymentMethod.zalopay) {
                order.setPaymentExpiresAt(LocalDateTime.now().plusMinutes(20));
                order.setRetryCount(0);
                order.setMaxRetries(1);
                orderRepository.save(order);

                PaymentLog log = PaymentLog.builder()
                        .order(order)
                        .paymentMethod(paymentMethod.name())
                        .paymentStatus("PENDING")
                        .amount(order.getFinalAmount())
                        .retryCount(0)
                        .build();
                paymentLogRepository.save(log);
            }

            // 7. Create order items
            System.out.println("7️⃣ Creating order items...");
            int itemCount = 0;
            for (CartItem cartItem : cart.getItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setProductName(cartItem.getProduct().getProductName());
                orderItem.setProductImage(cartItem.getProduct().getImageUrl());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getPrice());
                
                BigDecimal itemSubtotal = cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                orderItem.setSubtotal(itemSubtotal);
                
                orderItemRepository.save(orderItem);
                order.getItems().add(orderItem);
                itemCount++;
                
                System.out.println("   → Item " + itemCount + ": " + 
                                 cartItem.getProduct().getProductName() + 
                                 " x" + cartItem.getQuantity() + 
                                 " = " + itemSubtotal);

                Product product = cartItem.getProduct();
                int oldStock = product.getStockQuantity();
                int newStock = oldStock - cartItem.getQuantity();
                product.setStockQuantity(newStock);
                product.setSoldCount(product.getSoldCount() + cartItem.getQuantity());
                productRepository.save(product);
                
                System.out.println("   → Stock updated: " + oldStock + " → " + newStock);
            }
            System.out.println("   ✅ Created " + itemCount + " order items");

            // 8. Clear cart
            System.out.println("8️⃣ Clearing cart...");
            cartItemRepository.deleteByCart(cart);
            System.out.println("   ✅ Cart cleared");

            // 9. ✅ Create payment URL (VNPAY + ZALOPAY)
            Map<String, Object> result = new HashMap<>();
            result.put("order", mapToResponse(order));
            
            if (paymentMethod == PaymentMethod.vnpay) {
                System.out.println("9️⃣ Creating VNPay payment URL...");
                try {
                    String paymentUrl = vnPayService.createPaymentUrl(order, ipAddress);
                    result.put("paymentUrl", paymentUrl);
                    result.put("requiresPayment", true);
                    System.out.println("   ✅ VNPay URL created");
                } catch (UnsupportedEncodingException e) {
                    System.err.println("   ❌ Error creating VNPay URL: " + e.getMessage());
                    throw new BadRequestException("Error creating payment URL");
                }
            } else if (paymentMethod == PaymentMethod.zalopay) {
                System.out.println("9️⃣ Creating ZaloPay payment URL...");
                try {
                    String paymentUrl = zaloPayService.createPayment(order);
                    result.put("paymentUrl", paymentUrl);
                    result.put("requiresPayment", true);
                    System.out.println("   ✅ ZaloPay URL created");
                } catch (Exception e) {
                    System.err.println("   ❌ Error creating ZaloPay URL: " + e.getMessage());
                    throw new BadRequestException("Error creating ZaloPay payment URL: " + e.getMessage());
                }
            } else {
                result.put("requiresPayment", false);
            }

            // 10. Send email notifications
            try {
                orderEmailService.sendOrderPlacedSuccess(order);
                orderEmailService.sendNewOrderAlert(order);
            } catch (Exception e) {
                System.err.println("   ⚠️ Email notification failed: " + e.getMessage());
            }

            // 11. Gửi thông báo real-time
            webSocketService.broadcastNewOrder(order);
            webSocketService.sendKitchenNotification(order.getOrderId(), "NEW_ORDER", "Đơn mới #" + orderNumber);
            webSocketService.sendNotificationToUser(
                    order.getUser().getUserId(),
                    "Đơn hàng đã tạo",
                    "Đơn #" + orderNumber + " đã được tạo thành công",
                    "ORDER_CREATED"
            );

            try {
                eventPublisher.publishEvent(new OrderCreatedEvent(order));
            } catch (Exception e) {
                System.err.println("⚠️ Publish OrderCreatedEvent failed: " + e.getMessage());
            }

            try {
                OrderMessage orderMessage = mapToOrderMessage(order);
                rabbitMQProducer.sendOrderCreatedMessage(orderMessage);
                System.out.println("✅ Order message sent to RabbitMQ");
            } catch (Exception e) {
                System.err.println("⚠️ Failed to send message to RabbitMQ: " + e.getMessage());
            }

            System.out.println("═══════════════════════════════════════════════════════");
            System.out.println("✅ CREATE ORDER - SUCCESS");
            System.out.println("   Order ID: " + order.getOrderId());
            System.out.println("   Order Number: " + orderNumber);
            System.out.println("   Payment Method: " + paymentMethod);
            System.out.println("═══════════════════════════════════════════════════════\n");

            return result;
            
        } catch (Exception e) {
            System.err.println("═══════════════════════════════════════════════════════");
            System.err.println("❌ CREATE ORDER - FAILED");
            System.err.println("   Error: " + e.getClass().getSimpleName());
            System.err.println("   Message: " + e.getMessage());
            System.err.println("═══════════════════════════════════════════════════════");
            e.printStackTrace();
            throw e;
        }
    }

    // ... (GIỮ NGUYÊN TẤT CẢ CÁC METHODS KHÁC - chỉ copy phần dưới này)

    public OrderResponse getOrderById(Long orderId, String userEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseGet(() -> userRepository.findByUsername(userEmail)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")));

        if (!order.getUser().getUserId().equals(user.getUserId()) && 
            user.getRole() != UserRole.ROLE_ADMIN) {
            throw new BadRequestException("Access denied");
        }

        return mapToResponse(order);
    }

    public OrderResponse getOrderByNumber(String orderNumber, String userEmail) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseGet(() -> userRepository.findByUsername(userEmail)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")));

        if (!order.getUser().getUserId().equals(user.getUserId()) && 
            user.getRole() != UserRole.ROLE_ADMIN) {
            throw new BadRequestException("Access denied");
        }

        return mapToResponse(order);
    }

    public PageResponse<OrderResponse> getMyOrders(String userEmail, int page, int size) {
        User user = userRepository.findByEmail(userEmail)
                .orElseGet(() -> userRepository.findByUsername(userEmail)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orderPage = orderRepository.findByUser(user, pageable);

        return mapToPageResponse(orderPage);
    }

    public List<OrderResponse> getOrdersByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.findByEmail(username)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")));

        List<Order> orders = orderRepository.findByUserUserIdOrderByCreatedAtDesc(user.getUserId());
        return orders.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public PageResponse<OrderResponse> getAllOrders(int page, int size, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orderPage;

        if (status != null && !status.isEmpty()) {
            try {
                OrderStatus orderStatus = OrderStatus.valueOf(status.toLowerCase());
                orderPage = orderRepository.findByOrderStatus(orderStatus, pageable);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid order status");
            }
        } else {
            orderPage = orderRepository.findAll(pageable);
        }

        return mapToPageResponse(orderPage);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        if (request == null || request.getStatus() == null || request.getStatus().isBlank()) {
            throw new BadRequestException("Status is required");
        }
        return updateOrderStatus(orderId, request.getStatus());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        if (status == null || status.isBlank()) {
            throw new BadRequestException("Status is required");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        OrderStatus oldStatus = order.getOrderStatus();
        OrderStatus newStatus;
        try {
            String normalizedStatus = status.trim().toLowerCase();
            if ("shipping".equals(normalizedStatus)) {
                normalizedStatus = "delivering";
            }
            newStatus = OrderStatus.valueOf(normalizedStatus);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid order status");
        }

        order.setOrderStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        if (newStatus == OrderStatus.delivered && order.getPaymentMethod() == PaymentMethod.cash) {
            order.setPaymentStatus(PaymentStatus.paid);
        }

        if (newStatus == OrderStatus.cancelled && oldStatus != OrderStatus.cancelled) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                product.setSoldCount(product.getSoldCount() - item.getQuantity());
                productRepository.save(product);
            }
        }

        orderRepository.save(order);

        if (newStatus == OrderStatus.delivered) {
            try {
                orderEmailService.sendDeliveryCompleted(order);
            } catch (Exception e) {
                System.err.println("⚠️ Delivery email failed: " + e.getMessage());
            }
        }

        webSocketService.broadcastOrderStatusUpdate(order, oldStatus.name());
        webSocketService.sendOrderUpdateToUser(
                order.getUser() != null ? order.getUser().getUserId() : null,
                order
        );
        webSocketService.sendKitchenNotification(
                order.getOrderId(),
                "STATUS_UPDATED",
                "Trạng thái: " + oldStatus.name() + " → " + newStatus.name()
        );
        webSocketService.sendOrderUpdate(
                orderId,
                order.getOrderNumber(),
                newStatus.name().toUpperCase(),
                order.getUser() != null ? order.getUser().getEmail() : null
        );

        try {
            eventPublisher.publishEvent(new OrderStatusChangedEvent(order, oldStatus, newStatus));
        } catch (Exception e) {
            System.err.println("⚠️ Publish OrderStatusChangedEvent failed: " + e.getMessage());
        }

        try {
            OrderMessage orderMessage = mapToOrderMessage(order);
            rabbitMQProducer.sendOrderStatusUpdateMessage(orderMessage);
        } catch (Exception e) {
            System.err.println("⚠️ Failed to send status update to RabbitMQ: " + e.getMessage());
        }

        return mapToResponse(order);
    }

    private OrderMessage mapToOrderMessage(Order order) {
        List<OrderMessage.OrderItemMessage> itemMessages = order.getItems().stream()
                .map(item -> OrderMessage.OrderItemMessage.builder()
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        return OrderMessage.builder()
                .orderId(order.getOrderId())
                .orderNumber(order.getOrderNumber())
                .orderStatus(order.getOrderStatus().name())
                .paymentMethod(order.getPaymentMethod().name())
                .paymentStatus(order.getPaymentStatus().name())
                .userId(order.getUser() != null ? order.getUser().getUserId() : null)
                .customerName(order.getRecipientName())
                .customerEmail(order.getUser() != null ? order.getUser().getEmail() : null)
                .customerPhone(order.getPhone())
                .deliveryAddress(order.getDeliveryAddress())
                .totalAmount(order.getTotalAmount())
                .shippingFee(order.getShippingFee())
                .discount(order.getDiscountAmount())
                .items(itemMessages)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId, String username, CancelOrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        User user = userRepository.findByEmail(username)
                .orElseGet(() -> userRepository.findByUsername(username)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")));

        boolean isAdmin = user.getRole() == UserRole.ROLE_ADMIN;

        if (!isAdmin) {
            if (order.getUser() == null || !order.getUser().getUserId().equals(user.getUserId())) {
                throw new BadRequestException("Access denied");
            }

            if (order.getOrderStatus() != OrderStatus.pending) {
                throw new BadRequestException("Cannot cancel order in " + order.getOrderStatus() + " status");
            }

            // User chỉ được hủy trong vòng 5 phút từ thời điểm tạo đơn
            LocalDateTime createdAt = order.getCreatedAt();
            if (createdAt == null || createdAt.isBefore(LocalDateTime.now().minusMinutes(5))) {
                throw new BadRequestException("Bạn chỉ có thể hủy đơn trong vòng 5 phút sau khi đặt");
            }
        }

        OrderStatus oldStatus = order.getOrderStatus();
        order.setOrderStatus(OrderStatus.cancelled);
        order.setCancelledReason(request != null ? request.getReason() : null);
        order.setCancelledAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            product.setSoldCount(product.getSoldCount() - item.getQuantity());
            productRepository.save(product);
        }

        orderRepository.save(order);

        OrderCancellation cancellation = OrderCancellation.builder()
                .order(order)
                .cancelledBy(order.getUser())
                .reason(request != null ? request.getReason() : "User cancelled")
                .additionalNotes(request != null ? request.getAdditionalNotes() : null)
                .refundAmount(order.getTotalAmount() != null ? order.getTotalAmount().doubleValue() : null)
                .refundStatus("PENDING")
                .build();
        cancellationRepository.save(cancellation);

        webSocketService.sendOrderUpdate(
                orderId,
                order.getOrderNumber(),
                OrderStatus.cancelled.name().toUpperCase(),
                order.getUser() != null ? order.getUser().getEmail() : null
        );

        try {
            eventPublisher.publishEvent(new OrderStatusChangedEvent(order, oldStatus, OrderStatus.cancelled));
        } catch (Exception e) {
            System.err.println("⚠️ Publish OrderStatusChangedEvent (cancel) failed: " + e.getMessage());
        }

        return mapToResponse(order);
    }

    @Transactional
    public Map<String, Object> retryPayment(Long orderId, String username, String paymentMethodStr, String ipAddress) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        User user = userRepository.findByEmail(username)
                .orElseGet(() -> userRepository.findByUsername(username)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")));

        if (order.getUser() == null || !order.getUser().getUserId().equals(user.getUserId())) {
            throw new BadRequestException("Access denied");
        }

        if (order.getPaymentStatus() == PaymentStatus.paid) {
            throw new BadRequestException("Order already paid");
        }

        if (order.getRetryCount() != null && order.getMaxRetries() != null
                && order.getRetryCount() >= order.getMaxRetries()) {
            throw new BadRequestException("Maximum retry attempts reached");
        }

        PaymentMethod newPaymentMethod = PaymentMethod.cash;
        if ("VNPAY".equalsIgnoreCase(paymentMethodStr)) {
            newPaymentMethod = PaymentMethod.vnpay;
        } else if ("ZALOPAY".equalsIgnoreCase(paymentMethodStr)) {
            newPaymentMethod = PaymentMethod.zalopay;
        }

        order.setPaymentMethod(newPaymentMethod);
        order.setPaymentExpiresAt(LocalDateTime.now().plusMinutes(20));
        order.setRetryCount((order.getRetryCount() == null ? 0 : order.getRetryCount()) + 1);
        orderRepository.save(order);

        PaymentLog log = PaymentLog.builder()
                .order(order)
                .paymentMethod(newPaymentMethod.name())
                .paymentStatus("PENDING")
                .amount(order.getFinalAmount())
                .retryCount(order.getRetryCount())
                .build();
        paymentLogRepository.save(log);

        Map<String, Object> result = new HashMap<>();
        result.put("order", mapToResponse(order));

        try {
            if (newPaymentMethod == PaymentMethod.vnpay) {
                String paymentUrl = vnPayService.createPaymentUrl(order, ipAddress);
                result.put("paymentUrl", paymentUrl);
                result.put("requiresPayment", true);
            } else if (newPaymentMethod == PaymentMethod.zalopay) {
                String paymentUrl = zaloPayService.createPayment(order);
                result.put("paymentUrl", paymentUrl);
                result.put("requiresPayment", true);
            } else {
                result.put("requiresPayment", false);
            }
        } catch (Exception e) {
            throw new BadRequestException("Error creating payment URL: " + e.getMessage());
        }

        return result;
    }

    @Transactional
    public OrderResponse convertToCOD(Long orderId, String username) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        User user = userRepository.findByEmail(username)
                .orElseGet(() -> userRepository.findByUsername(username)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")));

        if (order.getUser() == null || !order.getUser().getUserId().equals(user.getUserId())) {
            throw new BadRequestException("Access denied");
        }

        if (order.getPaymentStatus() == PaymentStatus.paid) {
            throw new BadRequestException("Order already paid");
        }

        order.setPaymentMethod(PaymentMethod.cash);
        order.setPaymentStatus(PaymentStatus.pending);
        order.setPaymentExpiresAt(null);
        orderRepository.save(order);

        PaymentLog log = PaymentLog.builder()
                .order(order)
                .paymentMethod("CASH")
                .paymentStatus("CONVERTED_TO_COD")
                .amount(order.getFinalAmount())
                .retryCount(order.getRetryCount())
                .build();
        paymentLogRepository.save(log);

        return mapToResponse(order);
    }

    @Transactional(readOnly = false)
    public OrderResponse confirmCashPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getPaymentMethod() != PaymentMethod.cash) {
            throw new BadRequestException("Payment method is not CASH");
        }
        if (order.getPaymentStatus() != PaymentStatus.pending) {
            throw new BadRequestException("Payment status must be PENDING");
        }

        order.setPaymentStatus(PaymentStatus.paid);
        orderRepository.save(order);
        return mapToResponse(order);
    }

    @Transactional(readOnly = false)
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Xóa các bảng con trước để tránh lỗi FK (một số DB env có thể chưa áp dụng ON DELETE CASCADE).
        try {
            paymentLogRepository.deleteByOrderOrderId(orderId);
        } catch (Exception e) {
            System.err.println("⚠️ Failed to delete payment logs for order " + orderId + ": " + e.getMessage());
        }
        try {
            cancellationRepository.deleteByOrderOrderId(orderId);
        } catch (Exception e) {
            System.err.println("⚠️ Failed to delete cancellations for order " + orderId + ": " + e.getMessage());
        }

        // Admin có thể xóa bất kỳ đơn hàng nào.
        // Lưu ý về tồn kho:
        // - Nếu đơn đã "cancelled" thì tồn kho đã được hoàn lại khi hủy → không hoàn lại lần nữa.
        // - Nếu đơn đã "delivered" thì không hoàn lại tồn kho (tránh tăng stock sai).
        boolean shouldRevertInventory = order.getOrderStatus() != OrderStatus.cancelled
                && order.getOrderStatus() != OrderStatus.delivered;

        if (order.getItems() != null) {
            if (shouldRevertInventory) {
                for (OrderItem item : order.getItems()) {
                    Product product = item.getProduct();
                    if (product == null) {
                        continue;
                    }
                    int currentStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
                    int currentSold = product.getSoldCount() != null ? product.getSoldCount() : 0;
                    int qty = item.getQuantity() != null ? item.getQuantity() : 0;

                    product.setStockQuantity(currentStock + qty);
                    product.setSoldCount(Math.max(0, currentSold - qty));
                    productRepository.save(product);
                }
            }

            orderItemRepository.deleteAll(order.getItems());
        }

        orderRepository.delete(order);
    }

    private String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", (int)(Math.random() * 10000));
        return "ORD" + timestamp + random;
    }

    private void calculateDeliveryInfo(Order order) {
        if (order.getDeliveryLat() == null || order.getDeliveryLng() == null) {
            System.err.println("⚠️ Delivery coordinates missing - using default shipping fee");
            return;
        }

        try {
            // Use Haversine formula to calculate distance (no Google Maps API)
            double distanceKm = haversineDistanceKm(
                    RESTAURANT_LAT,
                    RESTAURANT_LNG,
                    order.getDeliveryLat(),
                    order.getDeliveryLng()
            );
            
            // Estimate duration (simple: ~30 km/h average speed in city)
            int durationMin = (int) Math.round((distanceKm / 30.0) * 60);

            order.setDistance(distanceKm > 0 ? distanceKm : null);
            order.setDuration(durationMin > 0 ? durationMin : null);
            if (durationMin > 0) {
                order.setEstimatedArrival(LocalDateTime.now().plusMinutes(durationMin));
            }

            BigDecimal subtotal = order.getSubtotal() != null ? order.getSubtotal() : BigDecimal.ZERO;
            BigDecimal discount = order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO;

            // Calculate shipping fee based on distance
            // Only free shipping if it's a FREE_SHIPPING promo
            BigDecimal fee = calculateShippingFee(distanceKm);
            
            // Check if shipping fee was already set to 0 by promo code
            boolean hasPromoFreeShipping = order.getShippingFee() != null
                    && order.getShippingFee().compareTo(BigDecimal.ZERO) == 0;
            
            if (!hasPromoFreeShipping) {
                order.setShippingFee(fee);
            }

            BigDecimal finalAmount = subtotal.subtract(discount).add(order.getShippingFee());
            order.setFinalAmount(finalAmount);
            order.setTotalAmount(finalAmount);
            
            System.out.println("   → Distance (Haversine): " + String.format("%.2f", distanceKm) + " km");
            System.out.println("   → Duration (estimated): " + durationMin + " min");
            System.out.println("   → Shipping fee (calculated): " + order.getShippingFee());
            System.out.println("   → Final amount (after delivery calc): " + finalAmount);
        } catch (Exception e) {
            System.err.println("⚠️ Could not calculate delivery info: " + e.getMessage());
        }
    }

    private double haversineDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371 * c;
    }

    private BigDecimal calculateShippingFee(double distanceKm) {
        // Ensure minimum 1 km (avoid returning fixed fee for 0 distance)
        if (distanceKm <= 0) {
            distanceKm = 1.0;
        }
        
        // 0-3 km: 15,000 VND
        if (distanceKm <= 3) {
            return DELIVERY_FEE;
        }
        
        // > 3 km: 15,000 + (distance - 3) * 5,000 per km
        BigDecimal base = DELIVERY_FEE;
        BigDecimal extraKm = BigDecimal.valueOf(distanceKm - 3);
        BigDecimal extra = extraKm.multiply(new BigDecimal("5000"));
        BigDecimal total = base.add(extra);
        
        // Round to nearest 5,000
        long rounded = Math.round(total.doubleValue() / 5000.0) * 5000;
        return BigDecimal.valueOf(rounded);
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        String orderNumber = order.getOrderNumber();
        if (orderNumber == null || orderNumber.isBlank()) {
            orderNumber = "ORD-" + order.getOrderId();
        }
        response.setOrderNumber(orderNumber);
        response.setOrderCode(orderNumber);
        response.setRecipientName(order.getRecipientName());
        response.setPhone(order.getPhone());
        response.setAddressLine(order.getDeliveryAddress());
        response.setSubtotal(order.getSubtotal());
        response.setShippingFee(order.getShippingFee());
        response.setDiscount(order.getDiscountAmount());
        response.setTotalAmount(order.getTotalAmount());
        response.setPaymentMethod(order.getPaymentMethod().name());
        response.setPaymentStatus(order.getPaymentStatus().name());
        response.setOrderStatus(order.getOrderStatus().name());
        response.setNotes(order.getNotes());
        response.setPaymentExpiresAt(order.getPaymentExpiresAt());
        response.setRetryCount(order.getRetryCount());
        response.setMaxRetries(order.getMaxRetries());
        response.setDeliveryLat(order.getDeliveryLat());
        response.setDeliveryLng(order.getDeliveryLng());
        response.setDistance(order.getDistance());
        response.setDuration(order.getDuration());
        response.setEstimatedArrival(order.getEstimatedArrival());
        response.setShortCode(buildShortCode(order));
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());

        List<OrderResponse.OrderItemResponse> items = order.getItems().stream()
                .map(this::mapItemToResponse)
                .collect(Collectors.toList());
        response.setItems(items);

        return response;
    }

    private OrderResponse.OrderItemResponse mapItemToResponse(OrderItem item) {
        OrderResponse.OrderItemResponse response = new OrderResponse.OrderItemResponse();
        response.setOrderItemId(item.getOrderItemId());
        response.setProductId(item.getProduct().getProductId());
        response.setProductName(item.getProductName());
        response.setProductImage(item.getProductImage());
        response.setQuantity(item.getQuantity());
        response.setPrice(item.getPrice());
        response.setSubtotal(item.getSubtotal());
        return response;
    }

    private PageResponse<OrderResponse> mapToPageResponse(Page<Order> page) {
        List<OrderResponse> content = page.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    private String buildShortCode(Order order) {
        if (order.getOrderNumber() != null && order.getOrderNumber().length() >= 6) {
            String tail = order.getOrderNumber()
                    .substring(order.getOrderNumber().length() - 6)
                    .toUpperCase();
            return tail;
        }
        if (order.getOrderId() != null) {
            String base36 = Long.toString(order.getOrderId(), 36).toUpperCase();
            if (base36.length() > 6) {
                base36 = base36.substring(base36.length() - 6);
            }
            return base36;
        }
        return "ORD";
    }
}
