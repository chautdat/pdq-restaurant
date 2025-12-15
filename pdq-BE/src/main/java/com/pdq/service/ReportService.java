package com.pdq.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pdq.dto.report.ProductSalesItem;
import com.pdq.dto.report.ReportResponse;
import com.pdq.entity.Order;
import com.pdq.entity.OrderItem;
import com.pdq.entity.PaymentMethod;
import com.pdq.entity.PaymentStatus;
import com.pdq.repository.OrderRepository;

@Service
@Transactional(readOnly = true)
public class ReportService {

    private final OrderRepository orderRepository;

    public ReportService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public ReportResponse getRevenueReport(String dateFromStr, String dateToStr) {
        LocalDateTime dateFrom = parseDate(dateFromStr, true);
        LocalDateTime dateTo = parseDate(dateToStr, false);

        System.out.println("========================================");
        System.out.println("Generating revenue report");
        System.out.println("Date range => from: " + dateFrom + " | to: " + dateTo);

        List<Order> paidOrders = fetchPaidOrders(dateFrom, dateTo);
        System.out.println("Paid orders fetched: " + paidOrders.size());

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal cashRevenue = BigDecimal.ZERO;
        BigDecimal vnpayRevenue = BigDecimal.ZERO;
        int cashOrders = 0;
        int vnpayOrders = 0;
        Map<Long, ProductSalesItem> productSalesMap = new HashMap<>();

        for (Order order : paidOrders) {
            BigDecimal orderAmount = order.getFinalAmount() != null ? order.getFinalAmount() : order.getTotalAmount();
            if (orderAmount == null) {
                orderAmount = BigDecimal.ZERO;
            }

            totalRevenue = totalRevenue.add(orderAmount);

            if (order.getPaymentMethod() == PaymentMethod.cash) {
                cashRevenue = cashRevenue.add(orderAmount);
                cashOrders++;
            } else if (order.getPaymentMethod() == PaymentMethod.vnpay) {
                vnpayRevenue = vnpayRevenue.add(orderAmount);
                vnpayOrders++;
            }

            if (order.getItems() == null) {
                continue;
            }

            for (OrderItem item : order.getItems()) {
                if (item == null || item.getProduct() == null) {
                    continue;
                }

                Long productId = item.getProduct().getProductId();
                String productName = item.getProductName();
                int quantity = item.getQuantity() != null ? item.getQuantity() : 0;
                BigDecimal unitPrice = item.getPrice() != null ? item.getPrice() : BigDecimal.ZERO;
                BigDecimal itemRevenue = item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO;

                ProductSalesItem salesItem = productSalesMap.get(productId);
                if (salesItem == null) {
                    salesItem = new ProductSalesItem(productId, productName, quantity, unitPrice, itemRevenue);
                    productSalesMap.put(productId, salesItem);
                } else {
                    int existingQuantity = salesItem.getQuantitySold() != null ? salesItem.getQuantitySold() : 0;
                    salesItem.setQuantitySold(existingQuantity + quantity);
                    salesItem.setTotalRevenue(salesItem.getTotalRevenue().add(itemRevenue));
                }
            }
        }

        List<ProductSalesItem> productSales = new ArrayList<>(productSalesMap.values());

        System.out.println("Revenue totals => total: " + totalRevenue + " | cash: " + cashRevenue + " | vnpay: " + vnpayRevenue);
        System.out.println("Order counts => total: " + paidOrders.size() + " | cash: " + cashOrders + " | vnpay: " + vnpayOrders);
        System.out.println("========================================");

        return new ReportResponse(
                totalRevenue,
                cashRevenue,
                vnpayRevenue,
                cashOrders,
                vnpayOrders,
                paidOrders.size(),
                productSales);
    }

    private List<Order> fetchPaidOrders(LocalDateTime dateFrom, LocalDateTime dateTo) {
        if (dateFrom != null && dateTo != null) {
            return orderRepository.findByPaymentStatusAndCreatedAtBetween(PaymentStatus.paid, dateFrom, dateTo);
        }
        if (dateFrom != null) {
            return orderRepository.findByPaymentStatusAndCreatedAtAfter(PaymentStatus.paid, dateFrom);
        }
        if (dateTo != null) {
            return orderRepository.findByPaymentStatusAndCreatedAtBefore(PaymentStatus.paid, dateTo);
        }
        return orderRepository.findByPaymentStatus(PaymentStatus.paid);
    }

    private LocalDateTime parseDate(String dateStr, boolean startOfDay) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        LocalDate date = LocalDate.parse(dateStr);
        return startOfDay ? date.atStartOfDay() : date.atTime(LocalTime.MAX);
    }
}
