package com.transaction.history.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Transaction {

    public static final String TRANSACTION_TIME_IS_INVALID = "Transaction Time is invalid";
    public static final String AMOUNT_IS_INVALID = "Amount is invalid";

    private String id;
    private Double amount;
    private Long transactionTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Long transactionTime) {
        this.transactionTime = transactionTime;
    }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (this.getAmount() == null || this.getAmount() <= 0) {
            errors.add(AMOUNT_IS_INVALID);
        }
        if(this.getTransactionTime() == null || this.getTransactionTime() <= 0){
            errors.add(TRANSACTION_TIME_IS_INVALID);
        }
        return errors;
    }

}
