package com.devgaze.uanghabis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "financial_setups")
public class FinancialSetup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "monthly_income")
    private BigDecimal monthlyIncome;

    @Column(name = "fixed_expense")
    private BigDecimal fixedExpense;

    @Column(name = "period_month")
    private int periodMonth;

    @Column(name = "period_year")
    private int periodYear;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
