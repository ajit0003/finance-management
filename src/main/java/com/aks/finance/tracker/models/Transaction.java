package com.aks.finance.tracker.models;

import com.aks.finance.tracker.enums.TransactionType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    
    private Long id;

    private TransactionType transactionType;

    private LocalDateTime date;

    private String transactionCode;

    private float amount;
}

