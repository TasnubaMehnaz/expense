package com.example.expense.domain;

public class Transaction {
    private Integer transactionId;
    private Integer categoryId;
    private Integer user_Id;
    private Double amount;
    private String note;
    private Long transactionDate;

    public Transaction(Integer transactionId, Integer categoryId, Integer user_Id, Double amount, String note, Long transactionDate) {
        this.transactionId = transactionId;
        this.categoryId = categoryId;
        this.user_Id = user_Id;
        this.amount = amount;
        this.note = note;
        this.transactionDate = transactionDate;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(Integer user_Id) {
        this.user_Id = user_Id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Long transactionDate) {
        this.transactionDate = transactionDate;
    }
}
