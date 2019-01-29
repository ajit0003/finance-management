package com.aks.finance.tracker.models;

import com.aks.finance.tracker.enums.Category;
import com.aks.finance.tracker.enums.Month;
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

    private Category budgetCategory;

    private Long id;
}
