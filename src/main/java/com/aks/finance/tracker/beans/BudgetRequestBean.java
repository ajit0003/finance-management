package com.aks.finance.tracker.beans;

import com.aks.finance.tracker.enums.TransactionCategory;
import java.time.Month;
import java.time.Year;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
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
public class BudgetRequestBean {

    @NotNull
    private Double budgetAmt;

    @NotNull
    private Month budgetMonth;

    @NotNull
    @FutureOrPresent
    private Year budgetYear;

    @NotNull
    private TransactionCategory budgetCatergory;
}
