package com.zeoharlem.gads.pepperedrice.models;

public class PaystackTransaction {
    private String reference;
    private String amount;
    private String transactionDate;
    private String status;

    public PaystackTransaction() {
    }

    public PaystackTransaction(String reference, String amount, String transactionDate, String status) {
        this.reference  = reference;
        this.amount     = amount;
        this.transactionDate = transactionDate;
        this.status     = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
