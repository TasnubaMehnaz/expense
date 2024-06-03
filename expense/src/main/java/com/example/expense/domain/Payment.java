package com.example.expense.domain;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
public class Payment {
    private String merchantId;
    private String orderId;
    private String dateTime;
    private String challenge;

    public Payment(String merchantId, String orderId, String dateTime, String challenge) {
        this.merchantId = merchantId;
        this.orderId = orderId;
        this.dateTime = dateTime;
        this.challenge = challenge;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }
}
