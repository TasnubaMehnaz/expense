package com.example.expense.repositories;

import com.example.expense.exceptions.EtBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository{

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_CREATE = "INSERT INTO ET_PAYMENT ( TRANSACTION_ID, MERCHANT_ID, ORDER_ID, DATE_TIME, PAYMENT_REF_ID) VALUES (?,?,?,?,?)";
    public PaymentRepositoryImpl() {
    }

    @Override
    public void addPaymentRecord(Integer transactionId, String merchantId, String orderId, String dateTime, String paymentRefId) throws EtBadRequestException {
        try {
            jdbcTemplate.update(SQL_CREATE,new Object[]{transactionId,merchantId,orderId,dateTime,paymentRefId});

        }catch (Exception e){
            throw new EtBadRequestException("Failed to update payment info!");
        }
    }
}
