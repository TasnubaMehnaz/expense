package com.example.expense.responseAPI;

public class CheckStatusResponse {
    private String merchantId;
    private String orderId;
    private String paymentRefId;
    private String amount;
    private String clientMobileNo;
    private String merchantMobileNo;
    private String orderDateTime;
    private String issuerPaymentDateTime;
    private String issuerPaymentRefNo;
    private String additionalMerchantInfo;
    private String status;
    private String statusCode;
    private String cancelIssuerDateTime;
    private String cancelIssuerRefNo;
    private String serviceType;

    public CheckStatusResponse( String merchantId, String orderId, String paymentRefId, String amount, String clientMobileNo, String merchantMobileNo, String orderDateTime, String issuerPaymentDateTime, String issuerPaymentRefNo, String additionalMerchantInfo, String status, String statusCode, String cancelIssuerDateTime, String cancelIssuerRefNo, String serviceType) {
        this.merchantId = merchantId;
        this.orderId = orderId;
        this.paymentRefId = paymentRefId;
        this.amount = amount;
        this.clientMobileNo = clientMobileNo;
        this.merchantMobileNo = merchantMobileNo;
        this.orderDateTime = orderDateTime;
        this.issuerPaymentDateTime = issuerPaymentDateTime;
        this.issuerPaymentRefNo = issuerPaymentRefNo;
        this.additionalMerchantInfo = additionalMerchantInfo;
        this.status = status;
        this.statusCode = statusCode;
        this.cancelIssuerDateTime = cancelIssuerDateTime;
        this.cancelIssuerRefNo = cancelIssuerRefNo;
        this.serviceType = serviceType;
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

    public String getPaymentRefId() {
        return paymentRefId;
    }

    public void setPaymentRefId(String paymentRefId) {
        this.paymentRefId = paymentRefId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getClientMobileNo() {
        return clientMobileNo;
    }

    public void setClientMobileNo(String clientMobileNo) {
        this.clientMobileNo = clientMobileNo;
    }

    public String getMerchantMobileNo() {
        return merchantMobileNo;
    }

    public void setMerchantMobileNo(String merchantMobileNo) {
        this.merchantMobileNo = merchantMobileNo;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getIssuerPaymentDateTime() {
        return issuerPaymentDateTime;
    }

    public void setIssuerPaymentDateTime(String issuerPaymentDateTime) {
        this.issuerPaymentDateTime = issuerPaymentDateTime;
    }

    public String getIssuerPaymentRefNo() {
        return issuerPaymentRefNo;
    }

    public void setIssuerPaymentRefNo(String issuerPaymentRefNo) {
        this.issuerPaymentRefNo = issuerPaymentRefNo;
    }

    public String getAdditionalMerchantInfo() {
        return additionalMerchantInfo;
    }

    public void setAdditionalMerchantInfo(String additionalMerchantInfo) {
        this.additionalMerchantInfo = additionalMerchantInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getCancelIssuerDateTime() {
        return cancelIssuerDateTime;
    }

    public void setCancelIssuerDateTime(String cancelIssuerDateTime) {
        this.cancelIssuerDateTime = cancelIssuerDateTime;
    }

    public String getCancelIssuerRefNo() {
        return cancelIssuerRefNo;
    }

    public void setCancelIssuerRefNo(String cancelIssuerRefNo) {
        this.cancelIssuerRefNo = cancelIssuerRefNo;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
