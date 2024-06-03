package com.example.expense.responseAPI;

public class PaymentResponse {
    private String sensitiveData;
    private String signature;

    public String getSensitiveData() {
        return sensitiveData;
    }

    public void setSensitiveData(String sensitiveData) {
        this.sensitiveData = sensitiveData;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public PaymentResponse(String sensitiveData, String signature) {
        this.sensitiveData = sensitiveData;
        this.signature = signature;
    }



}
