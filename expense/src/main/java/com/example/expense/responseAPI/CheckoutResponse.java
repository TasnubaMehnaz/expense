package com.example.expense.responseAPI;

public class CheckoutResponse {
    private String callBackUrl;
    private String status;

    public CheckoutResponse(String callBackUrl,String status) {
        this.callBackUrl = callBackUrl;
        this.status= status;
    }

    public String getCallBackURL() {
        return callBackUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCallBackURL(String callBackURL) {
        this.callBackUrl = callBackURL;
    }
}
