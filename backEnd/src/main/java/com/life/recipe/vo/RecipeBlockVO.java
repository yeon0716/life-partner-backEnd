package com.life.recipe.vo;

import java.util.Date;

import lombok.Data;

@Data
public class RecipeBlockVO {
    private Long blockId;
    private Long recipeId;
    private String blockType; // TEXT / IMAGE
    private String content;
    private Integer sortOrder;
    private Date createdAt;
}