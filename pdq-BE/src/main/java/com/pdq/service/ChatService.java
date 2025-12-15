package com.pdq.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdq.entity.Category;
import com.pdq.entity.Product;
import com.pdq.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Service
public class ChatService {

    @Value("${groq.api.key}")
    private String apiKey;

    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL = "llama-3.3-70b-versatile";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;

    public ChatService(ProductRepository productRepository) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.productRepository = productRepository;
    }

    public String chat(String userMessage) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = buildRequestBody(userMessage);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                GROQ_API_URL,
                HttpMethod.POST,
                request,
                String.class
            );

            return parseGroqResponse(response.getBody());

        } catch (Exception e) {
            System.err.println("❌ Error calling Groq API: " + e.getMessage());
            e.printStackTrace();
            return "Xin lỗi, tôi đang gặp sự cố kỹ thuật. Vui lòng thử lại sau! 🙏";
        }
    }

    private Map<String, Object> buildRequestBody(String userMessage) {
        String systemPrompt = buildSystemPrompt();
        String menuContext = getMenuContext();

        List<Map<String, String>> messages = new ArrayList<>();
        
        // System message
        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", systemPrompt + "\n\nMENU NHÀ HÀNG:\n" + menuContext);
        messages.add(systemMsg);

        // User message
        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        messages.add(userMsg);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", MODEL);
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 500);
        requestBody.put("top_p", 0.9);

        return requestBody;
    }

    private String buildSystemPrompt() {
        return """
            Bạn là trợ lý AI thân thiện của nhà hàng PDQ Restaurant.
            
            NHIỆM VỤ:
            - Tư vấn món ăn phù hợp với khách
            - Trả lời câu hỏi về menu, giá cả, giờ mở cửa
            - Gợi ý combo, món phổ biến
            - Luôn lịch sự, thân thiện, nhiệt tình
            
            NGUYÊN TẮC:
            1. Trả lời NGẮN GỌN (2-3 câu), dễ hiểu
            2. Dùng emoji phù hợp: 🍜 🍖 🥤 ✨
            3. Gợi ý 2-3 món cụ thể với giá
            4. Nếu không biết → nói thật + gợi ý liên hệ staff
            5. Kết thúc bằng câu hỏi thân thiện
            6. Chỉ nói không có món khi chắc chắn không xuất hiện trong menu; nếu tên gần giống, hãy gợi ý món tương tự hoặc phổ biến nhất

            PHONG CÁCH:
            - Thân thiện như bạn bè
            - Nhiệt tình nhưng không ép buộc
            - Hiểu nhu cầu khách (ngân sách, khẩu vị)
            """;
    }

    private String getMenuContext() {
        try {
            List<Product> products = productRepository.findByIsAvailableTrue();
            if (products != null && !products.isEmpty()) {
                return buildMenuFromProducts(products);
            }
        } catch (Exception e) {
            System.err.println("⚠️ Không thể lấy menu từ DB, dùng menu mặc định: " + e.getMessage());
        }
        return getFallbackMenuContext();
    }

    private String buildMenuFromProducts(List<Product> products) {
        products.sort(Comparator
            .comparing((Product p) -> getDisplayOrder(p.getCategory()))
            .thenComparing(p -> getCategoryName(p.getCategory()))
            .thenComparing(Product::getProductName, String.CASE_INSENSITIVE_ORDER));

        StringBuilder sb = new StringBuilder();
        String currentCategory = null;

        for (Product product : products) {
            String categoryName = getCategoryName(product.getCategory());
            if (!Objects.equals(categoryName, currentCategory)) {
                if (currentCategory != null) {
                    sb.append("\n");
                }
                sb.append("=== ").append(categoryName).append(" ===\n");
                currentCategory = categoryName;
            }

            BigDecimal salePrice = product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice();
            String description = product.getShortDescription();
            if (description == null || description.isBlank()) {
                description = product.getDescription();
            }

            sb.append("🍽️ ")
                .append(product.getProductName())
                .append(": ")
                .append(formatPrice(salePrice));

            if (description != null && !description.isBlank()) {
                sb.append(" - ").append(description.trim());
            }
            sb.append("\n");
        }

        return sb.toString().trim();
    }

    private String getCategoryName(Category category) {
        return category != null && category.getCategoryName() != null
            ? category.getCategoryName()
            : "Món khác";
    }

    private int getDisplayOrder(Category category) {
        if (category == null || category.getDisplayOrder() == null) {
            return Integer.MAX_VALUE;
        }
        return category.getDisplayOrder();
    }

    private String formatPrice(BigDecimal price) {
        if (price == null) {
            return "Liên hệ";
        }

        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        nf.setMaximumFractionDigits(0);
        nf.setMinimumFractionDigits(0);
        return nf.format(price) + "đ";
    }

    private String getFallbackMenuContext() {
        return """
            === MÓN CHÍNH ===
            🍖 Cơm gà xối mỡ: 50.000đ - Gà chiên giòn rụm, vàng thơm, ăn kèm cơm tơi
            🍖 Cơm chiên Dương Châu: 55.000đ - Cơm chiên thập cẩm, hải sản tươi
            🍖 Cơm gà Hải Nam: 50.000đ - Cơm gà thơm, nước chấm đặc biệt
            🍜 Phở bò tái: 60.000đ - Nước dùng hầm xương 8 tiếng
            🍜 Bún chả Hà Nội: 55.000đ - Chả nướng thơm, bún tươi
            
            === MÓN PHỤ ===
            🥗 Gỏi cuốn tôm thịt: 30.000đ - Tươi mát, ăn kèm nước chấm
            🥗 Chả giò rán: 35.000đ - Giòn tan, nhân đầy đặn
            🥗 Nem rán: 30.000đ - Truyền thống, giòn rụm
            
            === ĐỒ UỐNG ===
            🥤 Trà đá: 10.000đ - Giải khát, miễn phí refill
            🥤 Nước ngọt: 15.000đ - Coca, Pepsi, Sprite, Fanta
            🥤 Sinh tố bơ: 25.000đ - Bơ Đắk Lắk, béo ngậy
            🥤 Cà phê sữa đá: 20.000đ - Cà phê Robusta nguyên chất
            
            === TRÁNG MIỆNG ===
            🍰 Chè ba màu: 20.000đ - Mát lạnh, ngọt vừa
            🍰 Bánh flan: 15.000đ - Mềm mịn, caramel thơm
            
            === COMBO PHỔ BIẾN ===
            💰 Combo 1 người: 75.000đ (1 món chính + 1 đồ uống)
            💰 Combo 2 người: 150.000đ (2 món chính + 2 đồ uống + 1 món phụ)
            💰 Combo gia đình: 350.000đ (4 món chính + 4 đồ uống + 2 món phụ + 2 tráng miệng)
            
            === THÔNG TIN NHÀ HÀNG ===
            📍 Địa chỉ: 123 Nguyễn Huệ, Quận 1, TP.HCM
            📞 Hotline: 1900-xxxx
            ⏰ Giờ mở cửa:
               - Thứ 2-6: 8:00 - 22:00
               - Thứ 7, CN: 7:00 - 23:00
            🚗 Parking: Có chỗ đậu xe miễn phí
            💳 Thanh toán: Tiền mặt, Chuyển khoản, VNPay
            🎁 Ưu đãi: Giảm 10% cho đơn > 200k (Thứ 2-5)
            """;
    }

    private String parseGroqResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode choices = root.path("choices");
            
            if (choices.isArray() && choices.size() > 0) {
                JsonNode firstChoice = choices.get(0);
                JsonNode message = firstChoice.path("message");
                String content = message.path("content").asText();
                return content.trim();
            }

            System.err.println("❌ Unexpected Groq response format: " + responseBody);
            return "Xin lỗi, tôi không hiểu câu trả lời từ hệ thống. Vui lòng thử lại! 🙏";

        } catch (Exception e) {
            System.err.println("❌ Error parsing Groq response: " + e.getMessage());
            e.printStackTrace();
            return "Xin lỗi, có lỗi khi xử lý phản hồi. Vui lòng thử lại! 🙏";
        }
    }

    public boolean testConnection() {
        try {
            String response = chat("Xin chào!");
            return response != null && !response.contains("lỗi") && !response.contains("sự cố");
        } catch (Exception e) {
            return false;
        }
    }
}
