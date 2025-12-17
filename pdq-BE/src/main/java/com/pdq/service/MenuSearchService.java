package com.pdq.service;

import com.pdq.entity.Product;
import com.pdq.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class MenuSearchService {

    private final ProductRepository productRepository;

    public MenuSearchService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public String searchProducts(String keyword) {
        String normalized = normalizeKeyword(keyword);
        if (normalized.isEmpty()) {
            return getAllProducts();
        }

        // 1) Exact phrase match
        List<Product> products = productRepository.findByProductNameContainingIgnoreCase(normalized);

        // 2) If not found and keyword has multiple words -> try AND match by tokens
        if (products.isEmpty() && normalized.contains(" ")) {
            products = searchByAllTokens(normalized);
        }

        // 3) If still empty -> fallback OR match by tokens
        if (products.isEmpty() && normalized.contains(" ")) {
            products = searchByAnyToken(normalized);
        }

        if (products.isEmpty()) {
            return "❌ Không tìm thấy món ăn nào phù hợp với từ khóa: " + normalized;
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

    private String normalizeKeyword(String keyword) {
        if (keyword == null) {
            return "";
        }
        String normalized = keyword
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^\\p{L}\\p{N}\\s]", " ")
            .replaceAll("\\s+", " ")
            .trim();
        return normalized;
    }

    private List<Product> searchByAllTokens(String normalized) {
        String[] tokens = normalized.split(" ");
        List<Product> candidate = null;

        for (String t : tokens) {
            String token = t.trim();
            if (token.length() < 2) {
                continue;
            }

            List<Product> hit = productRepository.findByProductNameContainingIgnoreCase(token);
            if (candidate == null) {
                candidate = new ArrayList<>(hit);
                continue;
            }

            if (candidate.isEmpty()) {
                break;
            }

            Set<Long> hitIds = new HashSet<>();
            for (Product p : hit) {
                if (p.getProductId() != null) {
                    hitIds.add(p.getProductId());
                }
            }

            candidate.removeIf(p -> p.getProductId() == null || !hitIds.contains(p.getProductId()));
        }

        if (candidate == null) {
            return List.of();
        }

        candidate.sort(Comparator.comparing(Product::getProductName, String.CASE_INSENSITIVE_ORDER));
        return candidate;
    }

    private List<Product> searchByAnyToken(String normalized) {
        String[] tokens = normalized.split(" ");
        Set<Long> seen = new HashSet<>();
        List<Product> result = new ArrayList<>();

        for (String t : tokens) {
            String token = t.trim();
            if (token.length() < 2) {
                continue;
            }
            List<Product> hit = productRepository.findByProductNameContainingIgnoreCase(token);
            for (Product p : hit) {
                Long id = p.getProductId();
                if (id != null && seen.add(id)) {
                    result.add(p);
                }
            }
        }

        result.sort(Comparator.comparing(Product::getProductName, String.CASE_INSENSITIVE_ORDER));
        return result;
    }
}
