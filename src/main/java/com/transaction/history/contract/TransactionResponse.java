package com.transaction.history.contract;

import java.util.ArrayList;
import java.util.List;

public class TransactionResponse {


    private String status;

    private List<String> errors;


    public TransactionResponse() {
        this.errors = new ArrayList<>();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addErrors(List<String> errors) {
        if (errors != null) { this.errors.addAll(errors); }
    }
}
