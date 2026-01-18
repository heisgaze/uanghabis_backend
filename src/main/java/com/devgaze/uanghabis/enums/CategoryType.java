package com.devgaze.uanghabis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CategoryType {
    INCOME("Pemasukan"),
    EXPENSE("Pengeluaran");

    private final String label;
}
