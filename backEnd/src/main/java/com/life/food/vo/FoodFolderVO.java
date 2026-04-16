package com.life.food.vo;

import java.util.Date;

import lombok.Data;

@Data
public class FoodFolderVO {
    private Long folderId;
    private Long memberId;
    private String name;
    private Date createdAt;
}
