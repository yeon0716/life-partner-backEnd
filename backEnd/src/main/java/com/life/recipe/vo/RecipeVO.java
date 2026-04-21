package com.life.recipe.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class RecipeVO {
    private Long recipeId;
    private Long memberId;
    private Long categoryId;
    private String categoryName;
    
    private String title;
    private String description;
    private String cookingTime;
    private String servings;
    private String difficulty;
    
    private Date createdAt;
    private String thumbnailUrl;
    
    private Integer likeCount;        // 좋아요 개수
    private Integer liked;        // 내가 좋아요 했는지 (0/1)
    private Integer bookmarked;   // 내가 북마크 했는지 (0/1)
    
    private Long blockId;
    private String blockType;
    private String content;
    private Integer sortOrder;
    
    // 블록 리스트
    private List<RecipeBlockVO> blockList;
    private List<RecipeIngredientVO> ingredientList;
    private String keyword;
}