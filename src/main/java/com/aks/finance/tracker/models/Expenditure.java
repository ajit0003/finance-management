package com.aks.finance.tracker.models;

import com.aks.finance.tracker.enums.Month;
import java.time.Year;
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
public class Expenditure {

    private Long id;

    private Year year;

    private Month month;

    private Long categoryId;

    private Double amount;

}
