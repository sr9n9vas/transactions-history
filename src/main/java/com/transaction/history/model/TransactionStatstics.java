package com.transaction.history.model;

public class TransactionStatstics {

    private Double sumOfAmount;

    private Double averageOfAmount;

    private Double maximumAmount;

    private Double minimumAmount;

    private long noOfTransactions;

    public Double getSumOfAmount() {
        return sumOfAmount;
    }

    public void setSumOfAmount(Double sumOfAmount) {
        this.sumOfAmount = sumOfAmount;
    }

    public Double getAverageOfAmount() {
        return averageOfAmount;
    }

    public void setAverageOfAmount(Double averageOfAmount) {
        this.averageOfAmount = averageOfAmount;
    }

    public Double getMaximumAmount() {
        return maximumAmount;
    }

    public void setMaximumAmount(Double maximumAmount) {
        this.maximumAmount = maximumAmount;
    }

    public Double getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(Double minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public long getNoOfTransactions() {
        return noOfTransactions;
    }

    public void setNoOfTransactions(long noOfTransactions) {
        this.noOfTransactions = noOfTransactions;
    }
}
