package com.aks.finance.tracker.beans;

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
public class BudgetResponseBean {

    private Double budgetAmt;

    private Month budgetMonth;

    private Year budgetYear;

    private TransactionCategory budgetCatergory;

    private Long id;
}
