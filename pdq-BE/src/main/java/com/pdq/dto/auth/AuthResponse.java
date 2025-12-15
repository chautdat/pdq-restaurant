package com.pdq.dto.auth;

public class AuthResponse {
    private Boolean success;
    private String message;
    private String token;
    private String refreshToken;
    private UserInfo user;

    public AuthResponse() {}

    public AuthResponse(Boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }

    public AuthResponse(Boolean success, String message, String token, 
                       String refreshToken, UserInfo user) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public static AuthResponse success(String message, String token, UserInfo user) {
        return new AuthResponse(true, message, token, null, user);
    }

    public static AuthResponse error(String message) {
        return new AuthResponse(false, message, null);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    // Inner class for UserInfo
    public static class UserInfo {
        private Long userId;
        private String username;
        private String email;
        private String fullName;
        private String role;

        public UserInfo() {}

        public UserInfo(Long userId, String username, String email, 
                       String fullName, String role) {
            this.userId = userId;
            this.username = username;
            this.email = email;
            this.fullName = fullName;
            this.role = role;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
