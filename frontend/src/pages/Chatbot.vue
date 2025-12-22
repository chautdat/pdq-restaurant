<template>
  <div class="chatbot-container">
    <!-- Chat Button (Floating) -->
    <button
      v-if="!isOpen"
      class="chat-button"
      @click="toggleChat"
      title="Chat với AI"
    >
      <i class="fas fa-comments"></i>
      <span class="badge" v-if="unreadCount > 0">{{ unreadCount }}</span>
    </button>

    <!-- Chat Window -->
    <transition name="slide-up">
      <div v-if="isOpen" class="chat-window">
        <!-- Header -->
        <div class="chat-header">
          <div class="header-left">
            <div class="bot-avatar">🤖</div>
            <div class="header-info">
              <h3>PDQ Assistant</h3>
              <p class="status">
                <span class="status-dot"></span>
                {{ isTyping ? "Đang nhập..." : "Online" }}
              </p>
            </div>
          </div>
          <button class="btn-close" @click="toggleChat" title="Đóng">
            <i class="fas fa-times"></i>
          </button>
        </div>

        <!-- Messages -->
        <div class="chat-messages" ref="messagesContainer">
          <!-- Welcome Message -->
          <div class="message bot-message" v-if="messages.length === 0">
            <div class="message-bubble">
              <p>Xin chào! 👋 Tôi là trợ lý AI của nhà hàng PDQ.</p>
              <p>Tôi có thể giúp bạn:</p>
              <ul>
                <li>🍜 Tư vấn món ăn</li>
                <li>💰 Hỏi giá cả</li>
                <li>🎁 Gợi ý combo</li>
                <li>📍 Thông tin nhà hàng</li>
              </ul>
              <p>Bạn muốn hỏi gì? 😊</p>
            </div>
          </div>

          <!-- Message List -->
          <div
            v-for="(msg, index) in messages"
            :key="index"
            :class="[
              'message',
              msg.type === 'user' ? 'user-message' : 'bot-message',
            ]"
          >
            <div class="message-bubble">
              <p v-html="formatMessage(msg.text)"></p>
              <span class="message-time">{{ formatTime(msg.timestamp) }}</span>
            </div>
          </div>

          <!-- Typing Indicator -->
          <div v-if="isTyping" class="message bot-message">
            <div class="message-bubble">
              <div class="typing-indicator">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </div>
          </div>
        </div>

        <!-- Quick Suggestions -->
        <div
          class="quick-suggestions"
          v-if="showSuggestions && suggestions.length > 0"
        >
          <button
            v-for="(suggestion, index) in suggestions"
            :key="index"
            class="suggestion-btn"
            @click="sendSuggestion(suggestion)"
          >
            {{ suggestion }}
          </button>
        </div>

        <!-- Input Area -->
        <div class="chat-input-area">
          <textarea
            v-model="userInput"
            @keydown.enter.prevent="sendMessage"
            placeholder="Nhập tin nhắn..."
            rows="1"
            class="chat-input"
            :disabled="isTyping"
          ></textarea>
          <button
            class="btn-send"
            @click="sendMessage"
            :disabled="!userInput.trim() || isTyping"
            title="Gửi"
          >
            <i class="fas fa-paper-plane"></i>
          </button>
        </div>

        <!-- Footer -->
        <div class="chat-footer">
          <span>Powered by Google Gemini AI ✨</span>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import api from "@/axios";

export default {
  name: "ChatBot",

  data() {
    return {
      isOpen: false,
      userInput: "",
      messages: [],
      isTyping: false,
      unreadCount: 0,
      showSuggestions: true,
      suggestions: [
        "Món gì ngon?",
        "Giá phở bò bao nhiêu?",
        "Gợi ý combo cho 2 người",
        "Mấy giờ mở cửa?",
      ],
    };
  },

  methods: {
    toggleChat() {
      this.isOpen = !this.isOpen;
      if (this.isOpen) {
        this.unreadCount = 0;
        this.$nextTick(() => {
          this.scrollToBottom();
        });
      }
    },

    async sendMessage() {
      const message = this.userInput.trim();
      if (!message || this.isTyping) return;

      // Add user message
      this.messages.push({
        type: "user",
        text: message,
        timestamp: new Date(),
      });

      // Clear input
      this.userInput = "";
      this.showSuggestions = false;

      // Scroll to bottom
      this.$nextTick(() => {
        this.scrollToBottom();
      });

      // Show typing indicator
      this.isTyping = true;

      try {
        // Call API
        const response = await api.post("/chat", {
          message: message,
        });

        // Simulate delay for natural feel
        await this.delay(500);

        // Add bot response
        if (response.data && response.data.success) {
          this.messages.push({
            type: "bot",
            text: response.data.response,
            timestamp: new Date(),
          });
        } else {
          throw new Error("Invalid response");
        }
      } catch (error) {
        console.error("❌ Chat error:", error);
        this.messages.push({
          type: "bot",
          text: "Xin lỗi, tôi đang gặp sự cố kỹ thuật. Vui lòng thử lại sau! 🙏",
          timestamp: new Date(),
        });
      } finally {
        this.isTyping = false;
        this.$nextTick(() => {
          this.scrollToBottom();
        });
      }
    },

    sendSuggestion(suggestion) {
      this.userInput = suggestion;
      this.sendMessage();
    },

    formatMessage(text) {
      // Convert line breaks to <br>
      return text.replace(/\n/g, "<br>");
    },

    formatTime(date) {
      const hours = date.getHours().toString().padStart(2, "0");
      const minutes = date.getMinutes().toString().padStart(2, "0");
      return `${hours}:${minutes}`;
    },

    scrollToBottom() {
      const container = this.$refs.messagesContainer;
      if (container) {
        container.scrollTop = container.scrollHeight;
      }
    },

    delay(ms) {
      return new Promise((resolve) => setTimeout(resolve, ms));
    },
  },
};
</script>

<style scoped>
/* ========== FLOATING BUTTON ========== */
.chat-button {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #00b067 0%, #00d97e 100%);
  color: white;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 176, 103, 0.4);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  transition: all 0.3s;
  z-index: 9999;
}

.chat-button:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 16px rgba(0, 176, 103, 0.5);
}

.chat-button .badge {
  position: absolute;
  top: -5px;
  right: -5px;
  background: #ef4444;
  color: white;
  font-size: 0.7rem;
  font-weight: 700;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* ========== CHAT WINDOW ========== */
.chat-window {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 380px;
  height: 550px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  z-index: 9999;
  overflow: hidden;
}

/* ========== HEADER ========== */
.chat-header {
  background: linear-gradient(135deg, #00b067 0%, #00d97e 100%);
  color: white;
  padding: 1rem 1.2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 0.8rem;
}

.bot-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
}

.header-info h3 {
  margin: 0;
  font-size: 1.2rem;
  font-weight: 700;
}

.status {
  margin: 0.2rem 0 0 0;
  font-size: 0.95rem;
  display: flex;
  align-items: center;
  gap: 0.4rem;
  opacity: 0.9;
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #4ade80;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

.btn-close {
  background: none;
  border: none;
  color: white;
  font-size: 1.2rem;
  cursor: pointer;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.3s;
}

.btn-close:hover {
  background: rgba(255, 255, 255, 0.2);
}

/* ========== MESSAGES ========== */
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 1rem;
  background: #f8f9fa;
}

.message {
  display: flex;
  margin-bottom: 1rem;
}

.user-message {
  justify-content: flex-end;
}

.bot-message {
  justify-content: flex-start;
}

.message-bubble {
  max-width: 75%;
  padding: 0.8rem 1rem;
  border-radius: 12px;
  position: relative;
}

.user-message .message-bubble {
  background: linear-gradient(135deg, #00b067 0%, #00d97e 100%);
  color: white;
  border-bottom-right-radius: 4px;
}

.bot-message .message-bubble {
  background: white;
  color: #333;
  border-bottom-left-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.message-bubble p {
  margin: 0;
  font-size: 1.05rem;
  line-height: 1.5;
}

.message-bubble ul {
  margin: 0.5rem 0;
  padding-left: 1.2rem;
}

.message-bubble li {
  font-size: 1rem;
  margin: 0.3rem 0;
}

.message-time {
  display: block;
  font-size: 0.9rem;
  opacity: 0.6;
  margin-top: 0.3rem;
}

/* Typing Indicator */
.typing-indicator {
  display: flex;
  gap: 0.3rem;
  padding: 0.5rem 0;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #00b067;
  animation: typing 1.4s infinite;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%,
  60%,
  100% {
    transform: translateY(0);
    opacity: 0.5;
  }
  30% {
    transform: translateY(-10px);
    opacity: 1;
  }
}

/* ========== SUGGESTIONS ========== */
.quick-suggestions {
  padding: 0.5rem 1rem;
  background: white;
  border-top: 1px solid #e5e7eb;
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.suggestion-btn {
  padding: 0.5rem 0.8rem;
  border: 2px solid #e5e7eb;
  border-radius: 16px;
  background: white;
  color: #555;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.suggestion-btn:hover {
  border-color: #00b067;
  color: #00b067;
  background: #f0fff8;
}

/* ========== INPUT AREA ========== */
.chat-input-area {
  display: flex;
  gap: 0.8rem;
  padding: 1rem;
  background: white;
  border-top: 1px solid #e5e7eb;
}

.chat-input {
  flex: 1;
  padding: 0.8rem;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  font-size: 1.05rem;
  font-family: inherit;
  resize: none;
  max-height: 100px;
  transition: border 0.3s;
}

.chat-input:focus {
  outline: none;
  border-color: #00b067;
}

.chat-input:disabled {
  background: #f9fafb;
  cursor: not-allowed;
}

.btn-send {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  border: none;
  background: linear-gradient(135deg, #00b067 0%, #00d97e 100%);
  color: white;
  font-size: 1rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
  flex-shrink: 0;
}

.btn-send:hover:not(:disabled) {
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(0, 176, 103, 0.4);
}

.btn-send:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* ========== FOOTER ========== */
.chat-footer {
  padding: 0.6rem 1rem;
  background: #f9fafb;
  text-align: center;
  font-size: 0.9rem;
  color: #9ca3af;
  border-top: 1px solid #e5e7eb;
}

/* ========== ANIMATIONS ========== */
.slide-up-enter-active,
.slide-up-leave-active {
  transition: all 0.3s ease;
}

.slide-up-enter-from {
  transform: translateY(100px);
  opacity: 0;
}

.slide-up-leave-to {
  transform: translateY(100px);
  opacity: 0;
}

/* ========== SCROLLBAR ========== */
.chat-messages::-webkit-scrollbar {
  width: 6px;
}

.chat-messages::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: #00b067;
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: #009954;
}

/* ========== RESPONSIVE ========== */
@media (max-width: 480px) {
  .chat-window {
    width: calc(100vw - 20px);
    height: calc(100vh - 40px);
    bottom: 10px;
    right: 10px;
  }

  .chat-button {
    width: 55px;
    height: 55px;
    font-size: 1.3rem;
  }
}
</style>
