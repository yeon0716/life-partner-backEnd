package com.life.account.dto;

import lombok.Data;

@Data
public class CategoryDeltaDTO {
    private String categoryName;
    private int current;
    private int prev;
    private int delta;
    private double percent;
}