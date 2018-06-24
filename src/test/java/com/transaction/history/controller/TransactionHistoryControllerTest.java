package com.transaction.history.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.history.contract.TransactionRequest;
import com.transaction.history.contract.TransactionStatisticsResponse;
import com.transaction.history.model.Transaction;
import com.transaction.history.service.TransactionHistoryService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionHistoryController.class)
public class TransactionHistoryControllerTest {

    public static final String STATSTICS_URL = "/statistics";
    public static final String FAIL = "fail";
    public static final String SUCCESS = "success";
    public static final String TRANSACTIONS_URL = "/transactions";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionHistoryService transactionHistoryService;

    @Test
    public void testShouldReturnErrorsIfAmountIsInvalid() throws Exception {

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(-1000.0);
        transactionRequest.setTimestamp(1009098098);

        RequestBuilder requestBuilder = getRequestBuilder(transactionRequest);

        mvc.perform(requestBuilder).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", Matchers.is(FAIL)))
                .andExpect(jsonPath("$.errors.[0]", Matchers.is("Amount is invalid")))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
    }

    @Test
    public void testShouldReturnErrorsIfTimeIsNotProvidedInRequest() throws Exception {


        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(TRANSACTIONS_URL)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"amount\": 1000.0 }")
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", Matchers.is(FAIL)))
                .andExpect(jsonPath("$.errors.[0]", Matchers.is("Transaction Time is invalid")))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
    }

    @Test
    public void testShouldReturnErrorsIfTimeIsInvalid() throws Exception {


        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(TRANSACTIONS_URL)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"amount\": 1000.0, \"timestamp\": \"invalid\"}")
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder).andExpect(status().is4xxClientError());
    }

    @Test
    public void testShouldInvokeTransactionServiceAndReturnSuccess() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(1000.0);
        transactionRequest.setTimestamp(343453454);

        RequestBuilder requestBuilder = getRequestBuilder(transactionRequest);


        mvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(jsonPath("$.status", Matchers.is(SUCCESS)))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(0)));
        verify(transactionHistoryService, Mockito.times(1)).save(any(Transaction.class));
    }

    @Test
    public void testShouldReturnValidStatstics() throws Exception {

        TransactionStatisticsResponse response = getTransactionStatsticsResponse();

        when(transactionHistoryService.getStatstics()).thenReturn(response);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(STATSTICS_URL)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(jsonPath("$.avg", Matchers.is(1000.0)))
                .andExpect(jsonPath("$.sum", Matchers.is(1000.0)))
                .andExpect(jsonPath("$.max", Matchers.is(500.0)))
                .andExpect(jsonPath("$.min", Matchers.is(10.0)))
                .andExpect(jsonPath("$.count", Matchers.is(20)));
    }

    private TransactionStatisticsResponse getTransactionStatsticsResponse() {
        TransactionStatisticsResponse response = new TransactionStatisticsResponse();
        response.setAvg(1000.0);
        response.setSum(1000.0);
        response.setMax(500.0);
        response.setMin(10.0);
        response.setCount(20);
        return response;
    }

    @Test
    public void testShouldReturunErrorStatusCodeWhenServerFailsToRespond() throws Exception {

        TransactionStatisticsResponse response = getTransactionStatsticsResponse();
        when(transactionHistoryService.getStatstics()).thenThrow(new RuntimeException());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(STATSTICS_URL)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder).andExpect(status().isInternalServerError());

    }

    private RequestBuilder getRequestBuilder(TransactionRequest transactionRequest) {
        return MockMvcRequestBuilders.post(TRANSACTIONS_URL)
                .accept(MediaType.APPLICATION_JSON)
                .content(toJson(transactionRequest))
                .contentType(MediaType.APPLICATION_JSON);
    }


    private String toJson(TransactionRequest transactionRequest) {
        ObjectMapper mapperObj = new ObjectMapper();
        try {
            return mapperObj.writeValueAsString(transactionRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }

}