package com.pdq.event;

public class PasswordResetRequestedEvent {
    private final String email;
    private final String resetLink;

    public PasswordResetRequestedEvent(String email, String resetLink) {
        this.email = email;
        this.resetLink = resetLink;
    }

    public String getEmail() {
        return email;
    }

    public String getResetLink() {
        return resetLink;
    }
}
