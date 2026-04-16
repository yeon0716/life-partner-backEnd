package com.life.recipe.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class RecipeVO {
    private Long recipeId;
    private Long memberId;
    private String title;
    private Date createdAt;
    
    // 블록 리스트
    private List<RecipeBlockVO> blockList;
}