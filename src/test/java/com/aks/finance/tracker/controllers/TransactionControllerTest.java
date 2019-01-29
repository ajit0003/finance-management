package com.aks.finance.tracker.controllers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aks.finance.tracker.beans.TransactionRequestBean;
import com.aks.finance.tracker.beans.TransactionResponseBean;
import com.aks.finance.tracker.enums.TransactionType;
import com.aks.finance.tracker.models.Transaction;
import com.aks.finance.tracker.services.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @MockBean
    private TransactionService transactionService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("POST /transaction")
    public void testCreateTransaction() throws Exception {
        TransactionRequestBean bean = TransactionRequestBean.builder()
                                                            .amount(20f)
                                                            .transactionType(TransactionType.DEBIT)
                                                            .date(LocalDateTime.of(2019, 1, 28, 9, 00))
                                                            .build();

        TransactionResponseBean responseBean = TransactionResponseBean.builder()
                                                                      .amount(bean.getAmount())
                                                                      .date(bean.getDate())
                                                                      .transactionType(bean.getTransactionType())
                                                                      .build();

        doReturn(responseBean)
            .when(transactionService)
            .createTransaction(any(TransactionRequestBean.class));

        ResultActions resultActions = mockMvc.perform(post("/transaction")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(asJsonString(bean)))

        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        MvcResult result = resultActions.andReturn();
        String jsonResult = result.getResponse().getContentAsString();

        TransactionResponseBean actualResponseBean = parseJson(jsonResult, TransactionResponseBean.class);

        assertAll("Create Transaction Response",
                  () -> assertEquals(responseBean.getAmount(), actualResponseBean.getAmount(), 0),
                  () -> assertEquals(responseBean.getDate(), actualResponseBean.getDate()),
                  () -> assertEquals(responseBean.getTransactionType(), actualResponseBean.getTransactionType()),
                  () -> assertNotNull(actualResponseBean.getTransactionCode()));

    }

    public static String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseJson(String json, Class<T> tClass) {
        try {
            return new ObjectMapper().readValue(json, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}