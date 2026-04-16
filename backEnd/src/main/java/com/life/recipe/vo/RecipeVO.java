package com.life.recipe.vo;

import java.util.Date;

import lombok.Data;

@Data
public class RecipeVO {
    private Long recipeId;
    private Long memberId;
    private String title;
    private Date createdAt;
}