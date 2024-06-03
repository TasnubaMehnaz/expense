package com.example.expense.repositories;

import com.example.expense.exceptions.EtBadRequestException;

public interface PaymentRepository {
    void addPaymentRecord(Integer transactionId,String merchantId, String orderId, String dateTime, String paymentRefId) throws EtBadRequestException;
}
