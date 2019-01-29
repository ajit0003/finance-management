package com.aks.finance.tracker.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Month {

    JANUARY(1),
    FEBRUARY(2),
    MARCH(3),
    APRIL(4),
    MAY(4),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12);

    private int month;

    @JsonCreator
    public static Month fromValue(int value) {
        for(Month month : Month.values()) {
            if(month.getMonth() == value) {
                return month;
            }
        }
        return null;
    }
}
