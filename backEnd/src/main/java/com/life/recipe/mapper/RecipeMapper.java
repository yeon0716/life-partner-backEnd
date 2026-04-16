package com.life.recipe.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.life.recipe.vo.RecipeBlockVO;
import com.life.recipe.vo.RecipeVO;

import java.util.List;

@Mapper
public interface RecipeMapper {

    // 1. 레시피 등록
    void insertRecipe(RecipeVO recipe);

    // 2. 블록 등록 (여러 개)
    void insertRecipeBlockList(List<RecipeBlockVO> list);

    // 3. 레시피 리스트
    List<RecipeVO> selectRecipeList(int start, int end);

    // 4. 레시피 상세
    RecipeVO selectRecipeDetail(Long recipeId);

    // 5. 레시피 수정
    void updateRecipe(RecipeVO recipe);

    // 6. 블록 삭제
    void deleteBlocksByRecipeId(Long recipeId);

    // 7. 레시피 삭제
    void deleteRecipe(Long recipeId);

    // 8. 블록 내용 수정 (이미지 변경 등)
    void updateBlockContent(RecipeBlockVO block);

    // 9. 검색
    List<RecipeVO> searchRecipeList(String keyword, int start, int end);
}
