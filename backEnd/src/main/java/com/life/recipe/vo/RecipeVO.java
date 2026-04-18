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
    private Date createdAt;
    private String thumbnailUrl;
    
    private Integer likes;        // 좋아요 개수
    private Integer liked;        // 내가 좋아요 했는지 (0/1)
    private Integer bookmarked;   // 내가 북마크 했는지 (0/1)
    
    // 블록 리스트
    private List<RecipeBlockVO> blockList;
    private String keyword;
}