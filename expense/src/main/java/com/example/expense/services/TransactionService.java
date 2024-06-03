package com.example.expense.services;

import com.example.expense.domain.Transaction;
import com.example.expense.exceptions.EtBadRequestException;
import com.example.expense.exceptions.EtResourceNotFoundException;

import java.util.List;

public interface TransactionService {
    List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId);
    Transaction fetchTransactionById(Integer userId, Integer categoryId,Integer transactionId) throws EtResourceNotFoundException;
    Transaction addTransaction(Integer userId, Integer categoryId, Double amount, String note, Long transactionDate) throws EtBadRequestException;
    void updateTransaction(Integer userId, Integer categoryId, Integer TransactionId, Transaction transaction) throws EtBadRequestException;
    void removeTransaction(Integer userId, Integer categoryId, Integer transactionId) throws EtResourceNotFoundException;
    String fetchAmountByTransactionId(Integer transactionId) throws EtResourceNotFoundException;
}
