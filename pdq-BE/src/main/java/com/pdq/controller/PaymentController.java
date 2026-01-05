package com.pdq.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.pdq.dto.common.ApiResponse;
import com.pdq.entity.Order;
import com.pdq.entity.PaymentMethod;
import com.pdq.exception.BadRequestException;
import com.pdq.exception.ResourceNotFoundException;
import com.pdq.repository.OrderRepository;
import com.pdq.service.VNPayService;
import com.pdq.service.ZaloPayService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final VNPayService vnPayService;
    private final ZaloPayService zaloPayService;
    private final OrderRepository orderRepository;

    @Value("${app.frontend-url:http://localhost:8080}")
    private String frontendUrl;

    public PaymentController(
            VNPayService vnPayService,
            ZaloPayService zaloPayService,
            OrderRepository orderRepository) {

        this.vnPayService = vnPayService;
        this.zaloPayService = zaloPayService;
        this.orderRepository = orderRepository;
    }

    // ==========================
    // VNPAY CREATE PAYMENT
    // ==========================
    @PostMapping("/vnpay/create")
    public ResponseEntity<ApiResponse<Map<String, String>>> createVnpayPayment(
            @RequestParam String orderNumber,
            HttpServletRequest request) {

        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getPaymentMethod() != PaymentMethod.vnpay) {
            throw new BadRequestException("Order payment method is not VNPay");
        }

        try {
            String ipAddress = getIpAddress(request);
            String paymentUrl = vnPayService.createPaymentUrl(order, ipAddress);

            Map<String, String> resp = new HashMap<>();
            resp.put("paymentUrl", paymentUrl);
            resp.put("orderNumber", orderNumber);

            return ResponseEntity.ok(ApiResponse.success("Payment URL created", resp));

        } catch (UnsupportedEncodingException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error creating payment URL"));
        }
    }

    // ==========================
    // VNPAY CALLBACK → Verify & Redirect to Frontend
    // ==========================
    @GetMapping("/vnpay/callback")
    public RedirectView vnpayCallback(@RequestParam Map<String, String> params) {

        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("🔔 VNPAY CALLBACK RECEIVED");
        System.out.println("   Params: " + params);
        System.out.println("═══════════════════════════════════════════════════════");

        // ✅ Xử lý payment với backend
        boolean success = vnPayService.processPaymentCallback(params);

        String orderNumber = params.get("vnp_TxnRef");
        String transactionId = params.get("vnp_TransactionNo");
        String responseCode = params.get("vnp_ResponseCode");
        String amountStr = params.get("vnp_Amount");

        System.out.println("   Order: " + orderNumber);
        System.out.println("   Transaction: " + transactionId);
        System.out.println("   Response Code: " + responseCode);
        System.out.println("   Payment Success: " + success);

        // ✅ Nếu user HỦY thanh toán (responseCode = 24) → redirect về home
        if ("24".equals(responseCode)) {
            System.out.println("⚠️ User cancelled payment - Redirecting to home");
            return new RedirectView(frontendUrl + "/#/");
        }

        // ✅ Parse amount (VNPay sends amount * 100)
        long amount = 0;
        try {
            amount = Long.parseLong(amountStr) / 100;
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid amount format");
        }

        // ✅ BUILD frontend redirect URL
        StringBuilder redirectUrl = new StringBuilder(frontendUrl);
        redirectUrl.append("/#/payment/return?");

        // ✅ Add essential params for frontend display
        redirectUrl.append("orderNumber=").append(orderNumber).append("&");
        redirectUrl.append("transactionId=").append(transactionId != null ? transactionId : "N/A").append("&");
        redirectUrl.append("amount=").append(amount).append("&");
        redirectUrl.append("responseCode=").append(responseCode).append("&");
        redirectUrl.append("success=").append(success);

        System.out.println("🔄 Redirecting to: " + redirectUrl.toString());
        System.out.println("═══════════════════════════════════════════════════════\n");

        // ✅ REDIRECT người dùng về frontend
        return new RedirectView(redirectUrl.toString());
    }

    // ==========================
    // ZALOPAY CREATE PAYMENT
    // ==========================
    @PostMapping("/zalopay/create")
    public ResponseEntity<ApiResponse<Map<String, String>>> createZaloPay(
            @RequestParam String orderNumber) {

        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getPaymentMethod() != PaymentMethod.zalopay) {
            throw new BadRequestException("Order payment method is not ZaloPay");
        }

        try {
            String paymentUrl = zaloPayService.createPayment(order);

            Map<String, String> resp = new HashMap<>();
            resp.put("paymentUrl", paymentUrl);

            return ResponseEntity.ok(ApiResponse.success("ZaloPay URL created", resp));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ZaloPay error: " + e.getMessage()));
        }
    }

    // ==========================
    // ZALOPAY CALLBACK (server → server)
    // ==========================
    @PostMapping("/zalopay/callback")
    public Map<String, Object> zaloPayCallback(@RequestParam Map<String, Object> params) {

        boolean success = zaloPayService.processCallback(params);

        Map<String, Object> resp = new HashMap<>();
        resp.put("return_code", success ? 1 : 0);
        resp.put("return_message", success ? "Success" : "Fail");

        return resp;
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null || ip.isBlank()) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}