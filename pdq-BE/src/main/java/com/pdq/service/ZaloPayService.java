package com.pdq.service;

import com.pdq.config.ZaloPayConfig;
import com.pdq.entity.Order;
import com.pdq.entity.PaymentStatus;
import com.pdq.repository.OrderRepository;
import com.pdq.event.OrderPaidEvent;

import okhttp3.*;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ZaloPayService {

    private final ZaloPayConfig config;
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ZaloPayService(ZaloPayConfig config, OrderRepository orderRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.config = config;
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    private static final OkHttpClient client = new OkHttpClient();

    // ============================================
    // CREATE PAYMENT REQUEST
    // ============================================
    public String createPayment(Order order) throws Exception {
        
        System.out.println("\n🔵 ═══════════════════════════════════════════════════════");
        System.out.println("🔵 ZALOPAY CREATE PAYMENT - START");
        System.out.println("🔵 ═══════════════════════════════════════════════════════");

        long amount = order.getFinalAmount().longValue();
        String appTransId = generateTransId(order.getOrderNumber());
        long appTime = System.currentTimeMillis();
        
        // ✅ Tạo embed_data (bắt buộc)
        String embedData = "{}";  // Tối thiểu là object rỗng

        System.out.println("   → Order Number: " + order.getOrderNumber());
        System.out.println("   → Amount: " + amount);
        System.out.println("   → App Trans ID: " + appTransId);
        System.out.println("   → App Time: " + appTime);

        Map<String, Object> orderData = new HashMap<>();
        orderData.put("app_id", config.getAppId());
        orderData.put("app_trans_id", appTransId);
        orderData.put("app_user", "pdq_user_" + order.getUser().getUserId());
        orderData.put("amount", amount);
        orderData.put("app_time", appTime);              // ✅ THÊM
        orderData.put("embed_data", embedData);          // ✅ THÊM
        orderData.put("description", "Thanh toán đơn hàng #" + order.getOrderNumber());
        orderData.put("bank_code", "zalopayapp");
        orderData.put("item", "[]");
        orderData.put("callback_url", config.getCallbackUrl());

        // ====== Create MAC data ======
        String data = orderData.get("app_id") + "|" +
                      orderData.get("app_trans_id") + "|" +
                      orderData.get("app_user") + "|" +
                      orderData.get("amount") + "|" +
                      orderData.get("app_time") + "|" +
                      orderData.get("embed_data") + "|" +
                      orderData.get("item");

        String mac = hmacSHA256(config.getKey1(), data);
        orderData.put("mac", mac);

        System.out.println("   → Data for MAC: " + data);
        System.out.println("   → MAC: " + mac.substring(0, 20) + "...");
        
        // ✅ LOG TẤT CẢ PARAMS
        System.out.println("   📋 ALL PARAMS:");
        orderData.forEach((k, v) -> System.out.println("      " + k + " = " + v));

        // ====== Build HTTP request body ======
        FormBody.Builder body = new FormBody.Builder();
        orderData.forEach((k, v) -> body.add(k, v.toString()));

        Request request = new Request.Builder()
                .url(config.getEndpointCreate())
                .post(body.build())
                .build();

        System.out.println("   → Calling ZaloPay API: " + config.getEndpointCreate());

        Response response = client.newCall(request).execute();
        String json = response.body().string();

        System.out.println("   → ZaloPay Response: " + json);

        JSONObject obj = new JSONObject(json);

        if (obj.getInt("return_code") != 1) {
            System.err.println("❌ ZaloPay create failed: " + json);
            throw new RuntimeException("ZaloPay create failed: " + obj.getString("return_message"));
        }

        String orderUrl = obj.getString("order_url");
        
        System.out.println("✅ ZaloPay URL created: " + orderUrl);
        System.out.println("🔵 ═══════════════════════════════════════════════════════\n");

        return orderUrl;
    }

    // ============================================
    // CALLBACK FROM ZALOPAY (Server → Server)
    // ============================================
    public boolean processCallback(Map<String, Object> data) {

        System.out.println("\n🟣 ═══════════════════════════════════════════════════════");
        System.out.println("🟣 ZALOPAY CALLBACK RECEIVED");
        System.out.println("🟣 ═══════════════════════════════════════════════════════");
        System.out.println("   Callback Data: " + data);

        try {
            String appTransId = data.get("app_trans_id").toString();
            String zpTransId   = String.valueOf(data.get("zp_trans_id"));
            String amount      = String.valueOf(data.get("amount"));
            String mac         = data.get("mac").toString();

            System.out.println("   → App Trans ID: " + appTransId);
            System.out.println("   → ZP Trans ID: " + zpTransId);
            System.out.println("   → Amount: " + amount);
            System.out.println("   → MAC: " + mac.substring(0, 20) + "...");

            // ====== VERIFY MAC ======
            String rawMac = appTransId + "|" + zpTransId + "|" + amount;
            String generatedMac = hmacSHA256(config.getKey2(), rawMac);

            System.out.println("   → Generated MAC: " + generatedMac.substring(0, 20) + "...");

            if (!generatedMac.equals(mac)) {
                System.err.println("❌ ZaloPay MAC mismatch!");
                System.err.println("   Expected: " + generatedMac);
                System.err.println("   Received: " + mac);
                System.out.println("🟣 ═══════════════════════════════════════════════════════\n");
                return false;
            }

            System.out.println("✅ MAC verified successfully");

            // ====== Extract orderNumber from app_trans_id ======
            // Format: yyMMdd_orderNumber
            String orderNumber = appTransId.substring(appTransId.lastIndexOf("_") + 1);
            
            System.out.println("   → Extracted Order Number: " + orderNumber);

            Optional<Order> orderOpt = orderRepository.findByOrderNumber(orderNumber);
            if (orderOpt.isEmpty()) {
                System.err.println("❌ Order not found with number: " + orderNumber);
                System.out.println("🟣 ═══════════════════════════════════════════════════════\n");
                return false;
            }

            Order order = orderOpt.get();
            
            System.out.println("   → Order ID: " + order.getOrderId());
            System.out.println("   → Current Payment Status: " + order.getPaymentStatus());

            // ====== Update payment status ======
            order.setPaymentStatus(PaymentStatus.paid);
            orderRepository.save(order);

            System.out.println("✅ Payment status updated to PAID");
            System.out.println("🎉 ZaloPay Payment SUCCESS for order: " + orderNumber);
            try {
                eventPublisher.publishEvent(new OrderPaidEvent(order, order.getPaymentMethod()));
            } catch (Exception e) {
                System.err.println("⚠️ Publish OrderPaidEvent failed: " + e.getMessage());
            }
            System.out.println("🟣 ═══════════════════════════════════════════════════════\n");
            
            return true;

        } catch (Exception e) {
            System.err.println("❌ ZaloPay Callback ERROR: " + e.getMessage());
            e.printStackTrace();
            System.out.println("🟣 ═══════════════════════════════════════════════════════\n");
            return false;
        }
    }

    // ============================================
    // UTILITIES
    // ============================================

    /** 
     * Generate unique app_trans_id
     * Format: yyMMdd_orderNumber
     */
    private String generateTransId(String orderNumber) {
        String datePart = new SimpleDateFormat("yyMMdd").format(new Date());
        return datePart + "_" + orderNumber;
    }

    private String hmacSHA256(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmac.init(secretKey);
            byte[] hash = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));

            return sb.toString();

        } catch (Exception e) {
            System.err.println("❌ HMAC SHA256 Error: " + e.getMessage());
            return "";
        }
    }
}
