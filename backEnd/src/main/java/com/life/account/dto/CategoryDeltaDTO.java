package com.life.account.dto;

import lombok.Data;

@Data
public class CategoryDeltaDTO {
    private String categoryName;
    private int currentAmount;
    private int prevAmount;
    private int delta;
    private double percent;
}