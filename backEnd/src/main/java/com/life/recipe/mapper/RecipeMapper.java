package com.life.recipe.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.life.recipe.vo.RecipeBlockVO;
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
    								@Param("start") int start, 
    								@Param("end") int end);

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

}
