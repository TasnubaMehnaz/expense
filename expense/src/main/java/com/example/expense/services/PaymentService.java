package com.example.expense.services;

import com.example.expense.responseAPI.CheckoutResponse;
import com.example.expense.responseAPI.PaymentResponse;


public interface PaymentService {
    public PaymentResponse makePayment(String merchantId,String dateTime,String orderId,String challenge);
    public CheckoutResponse checkout(String merchantId,String orderId,String plainReceivedSensitiveData, Integer transactionId);
    public String decrypt(String encryptedData) ;

    public boolean verifySignature(String data, String signature);



}
