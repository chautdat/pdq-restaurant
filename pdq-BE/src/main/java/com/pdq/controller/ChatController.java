package com.pdq.controller;

import com.pdq.service.ChatService;
import com.pdq.service.MenuSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
// ✅ XÓA @CrossOrigin - Dùng WebConfig thống nhất
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MenuSearchService menuSearchService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");

        if (userMessage == null || userMessage.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(createErrorResponse("Tin nhắn không được để trống!"));
        }

        try {
            String lowerMessage = userMessage.toLowerCase().trim();

            boolean isMenuQuery = lowerMessage.contains("thực đơn") ||
                lowerMessage.contains("menu") ||
                lowerMessage.contains("món") ||
                lowerMessage.contains("có món") ||
                lowerMessage.contains("món nào") ||
                lowerMessage.contains("bán gì") ||
                lowerMessage.contains("có gì");

            boolean isSearchQuery = lowerMessage.contains("tìm") ||
                lowerMessage.contains("search") ||
                (lowerMessage.contains("có") && !lowerMessage.contains("không có"));

            // Ưu tiên tìm kiếm khi không hỏi trực tiếp về menu
            if (isSearchQuery && !lowerMessage.contains("thực đơn") && !lowerMessage.contains("menu")) {
                String keyword = extractKeyword(userMessage);
                String searchResult = menuSearchService.searchProducts(keyword);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("response", searchResult);
                response.put("timestamp", System.currentTimeMillis());

                return ResponseEntity.ok(response);
            }

            // ✅ KIỂM TRA CÂU HỎI VỀ MENU
            if (isMenuQuery) {
                String menuList = menuSearchService.getAllProducts();

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("response", menuList);
                response.put("timestamp", System.currentTimeMillis());

                return ResponseEntity.ok(response);
            }

            // ✅ KIỂM TRA TÌM KIẾM MÓN CỤ THỂ
            if (isSearchQuery) {
                String keyword = extractKeyword(userMessage);
                String searchResult = menuSearchService.searchProducts(keyword);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("response", searchResult);
                response.put("timestamp", System.currentTimeMillis());

                return ResponseEntity.ok(response);
            }

            // ✅ GỌI AI CHO CÂU HỎI KHÁC
            String assistantMessage = chatService.chat(userMessage);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("response", assistantMessage);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Đã xảy ra lỗi khi xử lý tin nhắn");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean isHealthy = chatService.testConnection();

            response.put("status", isHealthy ? "healthy" : "unhealthy");
            response.put("service", "groq-chat");
            response.put("model", "llama-3.1-70b-versatile");
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/suggestions")
    public ResponseEntity<Map<String, Object>> getSuggestions() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("suggestions", new String[]{
            "Món gì ngon?",
            "Giá phở bò bao nhiêu?",
            "Gợi ý combo cho 2 người",
            "Mấy giờ mở cửa?",
            "Có món nào không cay?",
            "Đồ uống có gì?",
            "Có chỗ đậu xe không?",
            "Món ăn chay có gì?"
        });

        return ResponseEntity.ok(response);
    }

    private String extractKeyword(String message) {
        String[] words = message.toLowerCase().split(" ");
        String[] skipWords = {"có", "món", "không", "tìm", "kiếm", "search", "xem", "thử", "được", "à", "ạ", "nhé"};

        for (String word : words) {
            word = word.trim();
            if (word.length() > 2 && !Arrays.asList(skipWords).contains(word)) {
                return word;
            }
        }

        return message;
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}
