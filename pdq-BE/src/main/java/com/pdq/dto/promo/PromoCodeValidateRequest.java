package com.pdq.dto.promo;

import java.math.BigDecimal;

public class PromoCodeValidateRequest {

    private String code;
    private BigDecimal orderAmount;

    public PromoCodeValidateRequest() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }
}
