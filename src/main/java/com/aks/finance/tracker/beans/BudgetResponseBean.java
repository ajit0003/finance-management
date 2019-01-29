package com.aks.finance.tracker.beans;

import com.aks.finance.tracker.enums.Month;
import com.aks.finance.tracker.enums.Category;
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

    private Category budgetCatergory;

    private Long id;
}
