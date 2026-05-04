package com.life.account.dto;

import lombok.Data;

@Data
public class MonthlySummaryDTO {
    private int totalIncome;
    private int totalExpense;
    private int balance;
}
