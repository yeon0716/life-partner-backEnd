package com.life.recipe.vo;

import lombok.Data;
import java.util.Date;

@Data
public class RecipeLikeVO {

    private Long likeId;      // LIKE_ID
    private Long memberId;    // MEMBER_ID
    private Long recipeId;    // RECIPE_ID
    private Date createdAt;   // CREATED_AT
}