package com.pdq.dto.order;

import jakarta.validation.constraints.NotBlank;

public class UpdateOrderStatusRequest {
    
    @NotBlank(message = "Status is required")
    private String status; // pending, confirmed, preparing, ready, delivering, delivered, cancelled

    private String note;

    public UpdateOrderStatusRequest() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}