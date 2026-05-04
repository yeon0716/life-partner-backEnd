package com.life.account.dto;

import java.util.List;

import lombok.Data;

@Data
public class MonthComparisonDTO {

    private int currentIncome;
    private int currentExpense;

    private int prevIncome;
    private int prevExpense;

    private double incomeChange;
    private double expenseChange;

    private List<CategoryDeltaDTO> categoryDeltas;
}
