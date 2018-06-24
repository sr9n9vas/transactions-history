package com.transaction.history.service;

import com.transaction.history.contract.TransactionStatisticsResponse;
import com.transaction.history.exception.TransactionHistoryException;
import com.transaction.history.model.Transaction;
import com.transaction.history.model.TransactionStatstics;
import com.transaction.history.repository.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionHistoryService {

    public static final String DATABASE_SAVE_ERROR_MESSAGE = "Could not create the transaction";
    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;

    public void save(Transaction transaction) throws TransactionHistoryException {

        int result = transactionHistoryRepository.save(transaction);
        if(result <= 0) throw new TransactionHistoryException(DATABASE_SAVE_ERROR_MESSAGE);
    }

    public TransactionStatisticsResponse getStatstics() throws TransactionHistoryException {
        TransactionStatstics transactionStatstics = transactionHistoryRepository.getStatstics();
        return getTransactionResponse(transactionStatstics);
    }

    private TransactionStatisticsResponse getTransactionResponse(TransactionStatstics transactionStatstics) {
        TransactionStatisticsResponse response = new TransactionStatisticsResponse();
        response.setCount(transactionStatstics.getNoOfTransactions());
        response.setMin(transactionStatstics.getMinimumAmount());
        response.setMax(transactionStatstics.getMaximumAmount());
        response.setSum(transactionStatstics.getSumOfAmount());
        response.setAvg(transactionStatstics.getAverageOfAmount());
        return response;
    }
}

