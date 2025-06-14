package com.example.expense.services;

import com.example.expense.domain.Transaction;
import com.example.expense.exceptions.EtBadRequestException;
import com.example.expense.exceptions.EtResourceNotFoundException;
import com.example.expense.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId) {

        return transactionRepository.findAll(userId, categoryId);
    }

    @Override
    public Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId) throws EtResourceNotFoundException {
        return transactionRepository.findById(userId,categoryId,transactionId);
    }

    @Override
    public String fetchAmountByTransactionId(Integer transactionId) throws EtResourceNotFoundException {
        return transactionRepository.findAmountById(transactionId);
    }

    @Override
    public Transaction addTransaction(Integer userId, Integer categoryId, Double amount, String note, Long transactionDate) throws EtBadRequestException {
        int transactionId=transactionRepository.create(userId, categoryId, amount, note, transactionDate);
        //System.out.println(transactionRepository.findById(userId,categoryId,transactionId));
        return transactionRepository.findById(userId,categoryId,transactionId);
    }

    @Override
    public void updateTransaction(Integer userId, Integer categoryId, Integer TransactionId, Transaction transaction) throws EtBadRequestException {
        transactionRepository.update(userId, categoryId, TransactionId, transaction);
    }

    @Override
    public void removeTransaction(Integer userId, Integer categoryId, Integer transactionId) throws EtResourceNotFoundException {
        transactionRepository.removeById(userId, categoryId, transactionId);
    }
}
