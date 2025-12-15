package com.pdq.controller;

import com.pdq.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebSocketController {

    private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);
    private final WebSocketService webSocketService;

    public WebSocketController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/kitchen/update")
    @SendTo("/topic/kitchen")
    public Map<String, Object> handleKitchenUpdate(@Payload Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>(payload == null ? Map.of() : payload);
        response.put("timestamp", System.currentTimeMillis());
        log.info("👩‍🍳 Kitchen update {}", response);
        return response;
    }

    @MessageMapping("/order/status")
    @SendTo("/topic/orders/status")
    public Map<String, Object> handleOrderStatus(@Payload Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>(payload == null ? Map.of() : payload);
        response.put("timestamp", System.currentTimeMillis());
        log.info("🔔 Order status update message {}", response);
        return response;
    }

    @MessageMapping("/notify/user")
    public void notifyUser(@Payload Map<String, Object> payload) {
        if (payload == null) {
            return;
        }
        Long userId = payload.get("userId") instanceof Number
                ? ((Number) payload.get("userId")).longValue()
                : null;
        String title = (String) payload.getOrDefault("title", "Thông báo");
        String body = (String) payload.getOrDefault("body", "");
        String type = (String) payload.getOrDefault("type", "INFO");

        if (userId != null) {
            webSocketService.sendNotificationToUser(userId, title, body, type);
        }
    }
}
