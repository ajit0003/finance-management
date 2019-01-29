package com.aks.finance.tracker.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionCategory {

    MEDICAL("Medical"),
    ENTERTAINMENT("Entertainment"),
    FOOD("Food"),
    TRAVEL("Travel"),
    MISCELLANEOUS("Miscellaneous");

    private String category;

    @JsonCreator
    public static TransactionCategory fromValue(String value) {
        for(TransactionCategory transactionCategory : TransactionCategory.values()) {
            if(transactionCategory.getCategory().equalsIgnoreCase(value)) {
                return transactionCategory;
            }
        }

        return null;
    }

}
