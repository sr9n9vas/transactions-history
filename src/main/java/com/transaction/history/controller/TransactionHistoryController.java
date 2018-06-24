package com.transaction.history.controller;

import com.transaction.history.contract.TransactionRequest;
import com.transaction.history.contract.TransactionResponse;
import com.transaction.history.contract.TransactionStatisticsResponse;
import com.transaction.history.exception.TransactionHistoryException;
import com.transaction.history.model.Transaction;
import com.transaction.history.service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class TransactionHistoryController {

    private static final Logger logger = Logger.getLogger(TransactionHistoryController.class.getName());

    public static final String FAIL = "fail";
    public static final String SUCCESS = "success";

    @Autowired
    TransactionHistoryService transactionHistoryService;


    @PostMapping("/transactions")
    public TransactionResponse createTransaction(@RequestBody TransactionRequest transactionRequest, HttpServletResponse response) {
        TransactionResponse transactionResponse = new TransactionResponse();
        Transaction transaction = toDomainTranscation(transactionRequest);
        List<String> errors = transaction.validate();
        if (!errors.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            transactionResponse.setStatus(FAIL);
            transactionResponse.addErrors(errors);
            return transactionResponse;
        }
        try {
            transactionHistoryService.save(transaction);
        } catch (TransactionHistoryException e) {
            transactionResponse.setStatus(FAIL);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errors.add(e.getError());
            logger.severe("TransactionResponse :: createTransaction, TransactionHistoryException"+e.getError());
            return transactionResponse;
        } catch (Exception e){
            transactionResponse.setStatus(FAIL);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            logger.severe("TransactionResponse :: createTransaction Unknown Exception");
        }
        transactionResponse.setStatus(SUCCESS);
        return transactionResponse;
    }

    @GetMapping("/statistics")
    public TransactionStatisticsResponse getStatstics(HttpServletResponse response){
        TransactionStatisticsResponse statstics = null;
        try {
            statstics = transactionHistoryService.getStatstics();
        }catch (Exception e){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return statstics;

    }

    private Transaction toDomainTranscation(TransactionRequest transactionRequest) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setTransactionTime(transactionRequest.getTimestamp());
        return transaction;
    }
}
