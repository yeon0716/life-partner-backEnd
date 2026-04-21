package com.life.recipe.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.life.recipe.vo.RecipeBlockVO;
import com.life.recipe.vo.RecipeCategoryVO;
import com.life.recipe.vo.RecipeIngredientVO;
import com.life.recipe.vo.RecipeVO;

import java.util.List;

@Mapper
public interface RecipeMapper {
	
    // 카테고리 리스트 조회
    List<RecipeCategoryVO> selectCategoryList();

    // =========================
    // 레시피
    // =========================
	
	// 1. 레시피 등록
    int insertRecipe(RecipeVO recipe);
    
    // 레시피 리스트
    List<RecipeVO> selectRecipeList(@Param("memberId") Long memberId,
    								@Param("keyword")String keyword,
    								@Param("categoryId")Long categoryId,
    								@Param("start") int start, 
    								@Param("end") int end);
    
    // 2. 레시피 상세
    RecipeVO selectRecipe(@Param("recipeId") Long recipeId);
    
    // 3. 레시피 수정
    int updateRecipe(RecipeVO recipe);
    
    // 4. 레시피 삭제
    int deleteRecipe(@Param("recipeId")Long recipeId);


    
    // =========================
    // 블록 (본문)
    // =========================
 
    // 1. 블록 등록
    int insertRecipeBlock(RecipeBlockVO block);
    
    // 2. 블록 삭제
    int deleteBlocksByRecipeId(@Param("recipeId")Long recipeId);

    // 3. 블록 내용 수정 (이미지 변경 등)
    int updateBlockContent(RecipeBlockVO block);
    
    // 4. 블록 상세보기
    List<RecipeBlockVO> selectRecipeBlocks(@Param("recipeId") Long recipeId);
    
    
    // =========================
    // 재료
    // =========================

    // 1. 재료 등록 (다건)
    int insertIngredient(RecipeIngredientVO ingredient);

    // 2. 재료 조회
    List<RecipeIngredientVO> selectIngredients(@Param("recipeId") Long recipeId);

    // 3. 재료 전체 삭제
    int deleteIngredients(@Param("recipeId") Long recipeId);
    
    // =========================
    // 좋아요 / 북마크
    // =========================

	// 9. 레시피 좋아요 존재여부
	int existsLike(@Param("memberId") Long memberId,
	         @Param("recipeId") Long recipeId); 
    
	// 10. 좋아요 등록
	int insertLike(@Param("memberId") Long memberId,
	            @Param("recipeId") Long recipeId);
	
	// 11. 좋아요 삭제
	int deleteLike(@Param("memberId") Long memberId,
	            @Param("recipeId") Long recipeId);
	
	// 11. 좋아요 삭제
	int deleteRecipeLike(@Param("recipeId") Long recipeId);

	// 12. 레시피 북마크 존재여부
	int existsBookmark(@Param("memberId") Long memberId,
	                @Param("recipeId") Long recipeId);
	
	// 13. 북마크 등록 
	int insertBookmark(@Param("memberId") Long memberId,
	                @Param("recipeId") Long recipeId);
	
	// 14. 북마크 삭제
	int deleteBookmark(@Param("memberId") Long memberId,
	                @Param("recipeId") Long recipeId);
	// 15. 북마크/좋아요 집계
	 RecipeVO selectRecipeStatus(
	            @Param("recipeId") Long recipeId,
	            @Param("memberId") Long memberId
	    );
	 
	// 14. 레시피 북맠 삭제
		int deleteRecipeBookmark(@Param("recipeId") Long recipeId);

}
