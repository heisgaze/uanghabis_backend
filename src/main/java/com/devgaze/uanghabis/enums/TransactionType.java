package com.devgaze.uanghabis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionType {
    INCOME("Pemasukan"),
    EXPENSE("Pengeluaran");

    private final String label;
}
