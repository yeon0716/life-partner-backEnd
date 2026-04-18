package com.life.recipe.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.life.recipe.vo.RecipeBlockVO;
import com.life.recipe.vo.RecipeCategoryVO;
import com.life.recipe.vo.RecipeVO;

import java.util.List;

@Mapper
public interface RecipeMapper {

    // 1. 레시피 등록
    int insertRecipe(RecipeVO recipe);

    // 2. 블록 등록
    int insertRecipeBlock(RecipeBlockVO block);

    // 3. 레시피 리스트
    List<RecipeVO> selectRecipeList(@Param("keyword")String keyword,
    								@Param("categoryId")Long categoryId,
    								@Param("start") int start, 
    								@Param("end") int end);
    
    // 🔥 카테고리 리스트 조회
    List<RecipeCategoryVO> selectCategoryList();

    // 4. 레시피 상세
    RecipeVO selectRecipeDetail(@Param("recipeId") Long recipeId);

    // 5. 레시피 수정
    int updateRecipe(RecipeVO recipe);

    // 6. 블록 삭제
    int deleteBlocksByRecipeId(@Param("recipeId")Long recipeId);

    // 7. 레시피 삭제
    int deleteRecipe(@Param("recipeId")Long recipeId);

    // 8. 블록 내용 수정 (이미지 변경 등)
    int updateBlockContent(RecipeBlockVO block);

	// 레시피 좋아요 존재여부
	int existsLike(@Param("memberId") Long memberId,
	         @Param("recipeId") Long recipeId); 
    
	// 좋아요 등록
	int insertLike(@Param("memberId") Long memberId,
	            @Param("recipeId") Long recipeId);
	
	// 좋아요 삭제
	int deleteLike(@Param("memberId") Long memberId,
	            @Param("recipeId") Long recipeId);

	// 레시피 북마크 존재여부
	int existsBookmark(@Param("memberId") Long memberId,
	                @Param("recipeId") Long recipeId);
	
	// 북마크 등록 
	int insertBookmark(@Param("memberId") Long memberId,
	                @Param("recipeId") Long recipeId);
	
	// 북마크 삭제
	int deleteBookmark(@Param("memberId") Long memberId,
	                @Param("recipeId") Long recipeId);

}
