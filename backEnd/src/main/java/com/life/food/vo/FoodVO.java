package com.life.food.vo;

import java.util.Date;

import lombok.Data;

@Data
public class FoodVO {
    private Long foodId;
    private Long folderId;
    private String name;
    private Date expiryDate;
    private Integer quantity;
    private String storageType;
    private String memo;
}