package com.transaction.history.service;

import com.transaction.history.exception.TransactionHistoryException;
import com.transaction.history.model.Transaction;
import com.transaction.history.model.TransactionStatstics;
import com.transaction.history.repository.TransactionHistoryRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionHistoryServiceTest {

    @InjectMocks
    private TransactionHistoryService transactionHistoryService;

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @Test
    public void testShouldInvokeTransactionRepostioty() throws TransactionHistoryException {
        Transaction transaction = getTransaction();
        when(transactionHistoryRepository.save(any(Transaction.class))).thenReturn(1);

        transactionHistoryService.save(transaction);

        Mockito.verify(transactionHistoryRepository, Mockito.times(1)).save(any(Transaction.class));
    }

    @Test(expected = TransactionHistoryException.class)
    public void testShouldThrowExceptionWhenCouldnotSaveTheTransaction() throws TransactionHistoryException {
        Transaction transaction = getTransaction();
            when(transactionHistoryRepository.save(any(Transaction.class))).thenReturn(0);
        try {
            transactionHistoryService.save(transaction);
        } catch (TransactionHistoryException e) {
            Assert.assertEquals("Could not create the transaction",e.getError());
            throw e;
        }

    }

    @Test
    public void testShouldInvokeStatsticSuccessfully() throws TransactionHistoryException {

        TransactionStatstics transactionStatstics =  new TransactionStatstics();
        transactionStatstics.setAverageOfAmount(100.0);
        transactionStatstics.setMinimumAmount(10.0);
        transactionStatstics.setMaximumAmount(100.0);
        transactionStatstics.setNoOfTransactions(12);
        transactionStatstics.setSumOfAmount(200.0);
        when(transactionHistoryRepository.getStatstics()).thenReturn(transactionStatstics);
        transactionHistoryService.getStatstics();

        verify(transactionHistoryRepository, times(1)).getStatstics();
    }

    private Transaction getTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAmount(178.0);
        transaction.setTransactionTime(new Long(1798798));
        return transaction;
    }

}