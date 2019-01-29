package com.aks.finance.tracker.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionType {

    CREDIT("Credit"),
    DEBIT("Debit");

    private String type;

    @JsonCreator
    public static TransactionType fromValue(String value) {
        for(TransactionType ttype : TransactionType.values()) {
            if(ttype.getType().equalsIgnoreCase(value)) {
                return ttype;
            }
        }

        return null;
    }
}