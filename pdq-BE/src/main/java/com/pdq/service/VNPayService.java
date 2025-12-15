package com.pdq.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pdq.config.VNPayConfig;
import com.pdq.entity.Order;
import com.pdq.entity.PaymentStatus;
import com.pdq.repository.OrderRepository;
import com.pdq.event.OrderPaidEvent;

@Service
public class VNPayService {

    private final VNPayConfig vnPayConfig;
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public VNPayService(VNPayConfig vnPayConfig, OrderRepository orderRepository,
                        ApplicationEventPublisher eventPublisher) {
        this.vnPayConfig = vnPayConfig;
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    // ----------------------------------------------------
    //  CREATE PAYMENT URL
    // ----------------------------------------------------
    public String createPaymentUrl(Order order, String ipAddress) throws UnsupportedEncodingException {

        System.out.println("\n💳 ═══════════════════════════════════════════════════════");
        System.out.println("💳 VNPAY CREATE PAYMENT - START");
        System.out.println("💳 ═══════════════════════════════════════════════════════");

        Map<String, String> vnpParams = new HashMap<>();

        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnPayConfig.getTmnCode());

        // VNPay requires amount *100
        String amount = order.getFinalAmount()
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .toPlainString();
        vnpParams.put("vnp_Amount", amount);

        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", order.getOrderNumber());
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang #" + order.getOrderNumber());
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
        vnpParams.put("vnp_IpAddr", ipAddress);

        // ✅ Tạo timestamp với đúng timezone Việt Nam (GMT+7)
        TimeZone vnTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        Calendar cld = Calendar.getInstance(vnTimeZone);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(vnTimeZone);

        String vnp_CreateDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_ExpireDate", vnp_ExpireDate);

        // ✅ LOG CHI TIẾT
        System.out.println("   → Order Number: " + order.getOrderNumber());
        System.out.println("   → Final Amount: " + order.getFinalAmount());
        System.out.println("   → VNPay Amount (*100): " + amount);
        System.out.println("   → IP Address: " + ipAddress);
        System.out.println("   🕐 Timestamps:");
        System.out.println("      Server Time: " + new Date());
        System.out.println("      vnp_CreateDate: " + vnp_CreateDate);
        System.out.println("      vnp_ExpireDate: " + vnp_ExpireDate);
        System.out.println("   → Return URL: " + vnPayConfig.getReturnUrl());

        // Sort keys
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnpParams.get(fieldName);

            if (fieldValue != null && fieldValue.length() > 0) {
                // Build hash data
                hashData.append(fieldName).append("=")
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));

                // Build query string
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()))
                        .append("=")
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));

                if (itr.hasNext()) {
                    hashData.append("&");
                    query.append("&");
                }
            }
        }

        System.out.println("   📋 Hash Data: "
                + hashData.toString().substring(0, Math.min(100, hashData.length())) + "...");

        String secureHash = hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        System.out.println("   → Secure Hash: " + secureHash.substring(0, 20) + "...");

        String finalUrl = vnPayConfig.getVnpayUrl() + "?" + query;

        System.out.println("   ✅ VNPay URL created");
        System.out.println("   🔗 URL: " + finalUrl.substring(0, Math.min(150, finalUrl.length())) + "...");
        System.out.println("💳 ═══════════════════════════════════════════════════════\n");

        return finalUrl;
    }

    // ----------------------------------------------------
    //  PROCESS CALLBACK
    // ----------------------------------------------------
    @Transactional
    public boolean processPaymentCallback(Map<String, String> params) {

        System.out.println("\n🟢 ═══════════════════════════════════════════════════════");
        System.out.println("🟢 VNPAY CALLBACK RECEIVED");
        System.out.println("🟢 ═══════════════════════════════════════════════════════");
        System.out.println("   Callback Params (RAW): " + params);

        // ✅ CHỈ LẤY PARAMS BẮT ĐẦU BẰNG "vnp_" để loại bỏ ngrok-skip-browser-warning
        Map<String, String> vnpParams = new HashMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey().startsWith("vnp_")) {
                vnpParams.put(entry.getKey(), entry.getValue());
            }
        }

        System.out.println("   Callback Params (FILTERED): " + vnpParams);

        String receivedHash = vnpParams.remove("vnp_SecureHash");
        vnpParams.remove("vnp_SecureHashType");

        String calculatedHash = hashAllFields(vnpParams);

        System.out.println("   → Received Hash: "
                + (receivedHash != null ? receivedHash.substring(0, 20) + "..." : "null"));
        System.out.println("   → Calculated Hash: " + calculatedHash.substring(0, 20) + "...");

        if (!calculatedHash.equalsIgnoreCase(receivedHash)) {
            System.err.println("   ❌ INVALID SIGNATURE!");
            System.out.println("🟢 ═══════════════════════════════════════════════════════\n");
            return false;
        }

        System.out.println("   ✅ Signature valid");

        String orderNumber = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");
        String amountParam = params.get("vnp_Amount");

        System.out.println("   → Order Number: " + orderNumber);
        System.out.println("   → Response Code: " + responseCode);
        System.out.println("   → Amount: " + amountParam);

        Optional<Order> orderOpt = orderRepository.findByOrderNumber(orderNumber);
        if (orderOpt.isEmpty()) {
            System.err.println("   ❌ Order not found: " + orderNumber);
            System.out.println("🟢 ═══════════════════════════════════════════════════════\n");
            return false;
        }

        Order order = orderOpt.get();

        // Validate Amount
        String expectedAmount = order.getFinalAmount()
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .toPlainString();

        System.out.println("   → Expected Amount: " + expectedAmount);

        if (!expectedAmount.equals(amountParam)) {
            System.err.println("   ❌ Amount mismatch!");
            System.err.println("      Expected: " + expectedAmount);
            System.err.println("      Received: " + amountParam);
            order.setPaymentStatus(PaymentStatus.failed);
            orderRepository.save(order);
            System.out.println("🟢 ═══════════════════════════════════════════════════════\n");
            return false;
        }

        // Payment success?
        if ("00".equals(responseCode)) {
            order.setPaymentStatus(PaymentStatus.paid);
            orderRepository.save(order);
            System.out.println("   ✅ Payment status updated to PAID");
            System.out.println("   🎉 VNPAY PAYMENT SUCCESS");
            try {
                eventPublisher.publishEvent(new OrderPaidEvent(order, order.getPaymentMethod()));
            } catch (Exception e) {
                System.err.println("⚠️ Publish OrderPaidEvent failed: " + e.getMessage());
            }
            System.out.println("🟢 ═══════════════════════════════════════════════════════\n");
            return true;
        }

        // Payment failed
        order.setPaymentStatus(PaymentStatus.failed);
        orderRepository.save(order);

        System.err.println("   ⚠️ VNPay returned failed code: " + responseCode);
        System.out.println("🟢 ═══════════════════════════════════════════════════════\n");
        return false;
    }

    // ----------------------------------------------------
    //  UTIL FUNCTIONS
    // ----------------------------------------------------

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKey);
            byte[] hashBytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes)
                sb.append(String.format("%02x", b));

            return sb.toString();

        } catch (Exception e) {
            System.err.println("❌ HMAC error: " + e.getMessage());
            return "";
        }
    }

    private String hashAllFields(Map<String, String> fields) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        StringBuilder sb = new StringBuilder();

        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {

            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);

            if (fieldValue != null && fieldValue.length() > 0) {
                try {
                    sb.append(fieldName).append("=")
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                } catch (UnsupportedEncodingException e) {
                    System.err.println("❌ URL encoding error: " + e.getMessage());
                }
            }

            if (itr.hasNext()) {
                sb.append("&");
            }
        }

        return hmacSHA512(vnPayConfig.getHashSecret(), sb.toString());
    }
}
