package com.pdq.controller;

import com.pdq.service.ChatService;
import com.pdq.service.MenuSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                lowerMessage.contains("món nào") ||
                lowerMessage.contains("các món") ||
                lowerMessage.contains("danh sách món") ||
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
        if (message == null) {
            return "";
        }

        // Normalize: lowercase + remove punctuation (keep letters/numbers/spaces)
        String normalized = message
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^\\p{L}\\p{N}\\s]", " ")
            .replaceAll("\\s+", " ")
            .trim();

        if (normalized.isBlank()) {
            return "";
        }

        // Case: "có <món> không/ko/k/hk" (e.g. "có cơm sườn không")
        Pattern coKhong = Pattern.compile(
            "\\b(có|co)\\b\\s+(.+?)\\s+\\b(không|khong|ko|k|hk|hông|hong)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );
        Matcher m1 = coKhong.matcher(normalized);
        if (m1.find()) {
            String keyword = m1.group(2).trim();
            // cleanup fillers
            keyword = keyword.replaceAll("^\\b(món|mon)\\b\\s+", "").trim();
            keyword = keyword.replaceAll("\\s+\\b(hay)\\b$", "").trim(); // "có ... hay không"
            if (!keyword.isBlank()) {
                return keyword;
            }
        }

        // Case: "tìm/kiếm/search <món>"
        Pattern findPattern = Pattern.compile(
            "\\b(tìm|tim|kiếm|kiem|search)\\b\\s+(?:món|mon)?\\s*(.+)$",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );
        Matcher m2 = findPattern.matcher(normalized);
        if (m2.find()) {
            String keyword = m2.group(2).trim();
            if (!keyword.isBlank()) {
                return keyword;
            }
        }

        // Fallback: remove common stop-words and keep the remaining phrase
        String[] words = normalized.split(" ");
        String[] skipWords = {
            "có","co","món","mon","không","khong","ko","k","hk","hông","hong",
            "tìm","tim","kiếm","kiem","search",
            "xem","thử","được","duoc","à","ạ","nhé","nhe","ơi","oi",
            "bạn","ban","anh","chị","chi","em","ad","admin",
            "cho","mình","minh","tôi","toi","muốn","muon","hỏi","hoi","về","ve",
            "quán","quan","nhà","nha","hàng","hang","nhàhàng","nhahang",
            "hay"
        };

        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            String word = w.trim();
            if (word.length() <= 1) {
                continue;
            }
            if (Arrays.asList(skipWords).contains(word)) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(word);
        }

        String keyword = sb.toString().trim();
        return keyword.isBlank() ? normalized : keyword;
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}
