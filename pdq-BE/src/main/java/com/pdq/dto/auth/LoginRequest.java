package com.pdq.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String username; // Thay email thành username

    @NotBlank(message = "Password is required")
    private String password;

    public LoginRequest() {}

    public LoginRequest(String username, String password) {
        this.username = username; // Sử dụng username thay vì email
        this.password = password;
    }

    public String getUsername() {
        return username; // Getter cho username
    }

    public void setUsername(String username) {
        this.username = username; // Setter cho username
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
