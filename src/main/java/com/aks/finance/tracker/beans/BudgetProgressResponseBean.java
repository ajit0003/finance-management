package com.aks.finance.tracker.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetProgressResponseBean {

    private Integer month;

    private Integer year;

    private Double totalBudget;

    private Double totalBalance;

    private Double entertainmentBalance;

    private Double foodBalance;

    private Double miscellaneousBalance;

    private Double travelBalance;

    private Double medicalBalance;
}
