package com.pdq.service;

import com.pdq.entity.Product;
import com.pdq.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuSearchService {

    private final ProductRepository productRepository;

    public MenuSearchService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public String searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }

        List<Product> products = productRepository.findByProductNameContainingIgnoreCase(keyword);

        if (products.isEmpty()) {
            return "❌ Không tìm thấy món ăn nào phù hợp với từ khóa: " + keyword;
        }

        return formatProductList(products);
    }

    public String getAllProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            return "Hiện tại chưa có món ăn nào trong thực đơn.";
        }

        return formatProductList(products);
    }

    private String formatProductList(List<Product> products) {
        StringBuilder result = new StringBuilder();
        result.append("🍽️ DANH SÁCH MÓN ĂN:\n\n");

        for (Product p : products) {
            result.append("📌 ").append(p.getProductName()).append("\n");
            result.append("   💰 Giá: ").append(formatPrice(p.getPrice())).append("đ\n");

            if (p.getDiscountPrice() != null && p.getDiscountPrice().doubleValue() > 0) {
                result.append("   🔥 Giảm giá: ").append(formatPrice(p.getDiscountPrice())).append("đ\n");
            }

            if (p.getDescription() != null && !p.getDescription().isEmpty()) {
                result.append("   📝 Mô tả: ").append(p.getDescription()).append("\n");
            }

            if (p.getStockQuantity() != null) {
                if (p.getStockQuantity() > 0) {
                    result.append("   ✅ Còn hàng: ").append(p.getStockQuantity()).append(" phần\n");
                } else {
                    result.append("   ❌ Hết hàng\n");
                }
            }

            result.append("\n");
        }

        return result.toString();
    }

    private String formatPrice(Object price) {
        if (price == null) {
            return "0";
        }

        double value = price instanceof Number ? ((Number) price).doubleValue() : 0;
        return String.format("%,.0f", value);
    }
}
