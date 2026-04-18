package com.life.recipe.service;

import com.life.recipe.vo.RecipeBlockVO;
import com.life.recipe.vo.RecipeCategoryVO;
import com.life.recipe.vo.RecipeVO;

import java.util.List;

public interface RecipeService {

    // 리스트
    List<RecipeVO> getRecipeList(String keyword, Long categoryId, int page, int size);

    // 상세
    RecipeVO getRecipeDetail(Long recipeId);
    
    List<RecipeCategoryVO> getCategoryList();

    // 등록
    int createRecipe(RecipeVO recipe);

    // 수정
    int updateRecipe(RecipeVO recipe);

    // 삭제
    int deleteRecipe(Long recipeId);

    // 이미지 수정 (블록 단위)
    int updateBlock(RecipeBlockVO block);
    
    // 레시피 좋아요
    boolean toggleLike(Long memberId, Long recipeId);
    
    // 레시피 북마크
    boolean toggleBookmark(Long memberId, Long recipeId);
    
}
