package com.aks.finance.tracker.models;

import com.aks.finance.tracker.enums.TransactionCategory;
import java.time.Month;
import java.time.Year;
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
public class Budget {

    private Double budgetAmount;

    private Month budgetMonth;

    private Year budgetYear;

    private TransactionCategory budgetCategory;

    private Long id;
}
