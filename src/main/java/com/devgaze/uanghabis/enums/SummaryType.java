package com.devgaze.uanghabis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SummaryType {
    AMAN("Aman"),
    WASPADA("Waspada"),
    BAHAYA("Bahaya");

    private String label;
}
