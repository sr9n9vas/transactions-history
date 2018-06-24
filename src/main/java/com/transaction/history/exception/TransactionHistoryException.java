package com.transaction.history.exception;

public class TransactionHistoryException extends Exception {

    private  String error;
    public TransactionHistoryException() {
        super();
    }
    public TransactionHistoryException(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
