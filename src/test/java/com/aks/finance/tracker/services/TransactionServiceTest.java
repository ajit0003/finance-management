package com.aks.finance.tracker.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.aks.finance.tracker.beans.TransactionRequestBean;
import com.aks.finance.tracker.beans.TransactionResponseBean;
import com.aks.finance.tracker.enums.TransactionType;
import com.aks.finance.tracker.models.Transaction;
import com.aks.finance.tracker.repositories.TransactionRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class TransactionServiceTest {

    @MockBean
    private TransactionRepository transactionRepository;

    private TransactionService transactionService;

    @BeforeEach
    public void init() {
        this.transactionService = new TransactionService(transactionRepository);
    }

    @Test
    public void createReservationTest() {
        //Arrange
        TransactionRequestBean requestBean = TransactionRequestBean.builder()
                                                                   .amount(20f)
                                                                   .transactionType(TransactionType.DEBIT)
                                                                   .date(LocalDateTime.of(2019, 1, 28, 9, 00))
                                                                   .build();

        Transaction transaction = Transaction.builder()
                                             .amount(requestBean.getAmount())
                                             .transactionType(requestBean.getTransactionType())
                                             .date(requestBean.getDate())
                                             .id(1L)
                                             .transactionCode(UUID.randomUUID().toString())
                                             .build();

        doReturn(transaction).when(transactionRepository).save(any(Transaction.class));

        //Act
        TransactionResponseBean optionalTransactionResponseBean =  transactionService.createTransaction(requestBean);

        //Assert
        verify(transactionRepository, times(1)).save( argThat(t -> {
            assertAll("Transaction Object on Save",
                      () -> assertEquals(requestBean.getAmount(), t.getAmount()),
                      () -> assertEquals(requestBean.getTransactionType(), t.getTransactionType()),
                      () -> assertEquals(requestBean.getDate(), t.getDate()),
                      () -> assertNotNull(t.getTransactionCode()));
            return true;
        }));

    }

}
