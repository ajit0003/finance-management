package com.aks.finance.tracker.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {

    MEDICAL("Medical"),
    ENTERTAINMENT("Entertainment"),
    FOOD("Food"),
    TRAVEL("Travel"),
    MISCELLANEOUS("Miscellaneous");

    private String category;

    @JsonCreator
    public static Category fromValue(String value) {
        for(Category category : Category.values()) {
            if(category.getCategory().equalsIgnoreCase(value)) {
                return category;
            }
        }

        return null;
    }

}
