package com.devgaze.uanghabis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DecisionType {
    AMAN("Aman"),
    TUNDA("Tunda"),
    BERESIKO("Beresiko");

    private String label;
}
