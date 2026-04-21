package com.life.recipe.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.life.recipe.mapper.RecipeMapper;
import com.life.recipe.vo.RecipeBlockVO;
import com.life.recipe.vo.RecipeCategoryVO;
import com.life.recipe.vo.RecipeIngredientVO;
import com.life.recipe.vo.RecipeVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeMapper recipeMapper;

    // =========================
    // 📌 리스트
    // =========================
    @Override
    public List<RecipeVO> getRecipeList(Long memberId, String keyword, Long categoryId, int page, int size) {

        int start = (page - 1) * size + 1;
        int end = page * size;

        List<RecipeVO> list = recipeMapper.selectRecipeList(
            memberId, keyword, categoryId, start, end
        );
        
        System.out.println("list --" + list);

        // null 방어 + 기본값 보정
        if (list != null) {
            for (RecipeVO recipe : list) {
                // 로그인 안했을 때 기본값
                if (memberId == null) {
                    recipe.setLiked(0);
                    recipe.setBookmarked(0);
                } else {
                    // DB에서 0/1 그대로 내려오므로 안전 처리
                    recipe.setLiked(recipe.getLiked() == null ? 0 : recipe.getLiked());
                    recipe.setBookmarked(recipe.getBookmarked() == null ? 0 : recipe.getBookmarked());
                }
            }
        }
        return list;
    }
    // =========================
    // 📌 카테고리
    // =========================
    @Override
    public List<RecipeCategoryVO> getCategoryList() {
        return recipeMapper.selectCategoryList();
    }

    @Override
    public RecipeVO getRecipeDetail(Long memberId, Long recipeId) {

        // 1. 기본 레시피
        RecipeVO recipe = recipeMapper.selectRecipe(recipeId);

        if (recipe == null) {
            throw new RuntimeException("RECIPE NOT FOUND");
        }

        // 2. 블록
        recipe.setBlockList(recipeMapper.selectRecipeBlocks(recipeId));

        // 3. 재료
        recipe.setIngredientList(recipeMapper.selectIngredients(recipeId));

        // 4. 좋아요 / 북마크 상태
        RecipeVO status = null;

        if (memberId != null) {
            status = recipeMapper.selectRecipeStatus(recipeId, memberId);
        }

        // 좋아요 여부
        recipe.setLiked(
            status != null ? status.getLiked() : 0
        );

        // 북마크 여부
        recipe.setBookmarked(
            status != null ? status.getBookmarked() : 0
        );

        return recipe;
    }
    // =========================
    // 📌 등록
    // =========================
    @Override
    @Transactional
    public int createRecipe(RecipeVO recipe) {

        // -------------------------
        // 0. 데이터 정리
        // -------------------------
        if (recipe.getBlockList() != null) {
            recipe.setBlockList(
                recipe.getBlockList().stream()
                    .filter(b -> b.getContent() != null && !b.getContent().isBlank())
                    .toList()
            );
        }

        if (recipe.getIngredientList() != null) {
            recipe.setIngredientList(
                recipe.getIngredientList().stream()
                    .filter(i -> i.getName() != null && !i.getName().isBlank())
                    .toList()
            );
        }

        // -------------------------
        // 1. 썸네일
        // -------------------------
        recipe.setThumbnailUrl(extractThumbnail(recipe.getBlockList()));

        // -------------------------
        // 2. 레시피 insert
        // -------------------------
        int result = recipeMapper.insertRecipe(recipe);

        if (result != 1) {
            throw new RuntimeException("RECIPE INSERT FAILED");
        }

        Long recipeId = recipe.getRecipeId();

        // -------------------------
        // 3. 블록 insert
        // -------------------------
        if (recipe.getBlockList() != null && !recipe.getBlockList().isEmpty()) {

            for (int i = 0; i < recipe.getBlockList().size(); i++) {
                RecipeBlockVO block = recipe.getBlockList().get(i);
                block.setRecipeId(recipeId);
                block.setSortOrder(i + 1);

                recipeMapper.insertRecipeBlock(block);
            }
        }

        // -------------------------
        // 4. 재료 insert (🔥 FIX)
        // -------------------------
        if (recipe.getIngredientList() != null && !recipe.getIngredientList().isEmpty()) {

            for (RecipeIngredientVO ingredient : recipe.getIngredientList()) {
                ingredient.setRecipeId(recipeId);
                recipeMapper.insertIngredient(ingredient);
            }
        }

        return 1;
    }

    // =========================
    // 📌 수정
    // =========================
    @Override
    @Transactional
    public int updateRecipe(RecipeVO recipe) {

        // -------------------------
        // 0. 데이터 정리
        // -------------------------
        if (recipe.getBlockList() != null) {
            recipe.setBlockList(
                recipe.getBlockList().stream()
                    .filter(b -> b.getContent() != null && !b.getContent().isBlank())
                    .toList()
            );
        }

        if (recipe.getIngredientList() != null) {
            recipe.setIngredientList(
                recipe.getIngredientList().stream()
                    .filter(i -> i.getName() != null && !i.getName().isBlank())
                    .toList()
            );
        }

        // -------------------------
        // 1. 썸네일
        // -------------------------
        recipe.setThumbnailUrl(extractThumbnail(recipe.getBlockList()));

        // -------------------------
        // 2. 레시피 update
        // -------------------------
        int result = recipeMapper.updateRecipe(recipe);

        if (result != 1) {
            throw new RuntimeException("RECIPE UPDATE FAILED");
        }

        Long recipeId = recipe.getRecipeId();

        // -------------------------
        // 3. 블록 처리
        // -------------------------
        recipeMapper.deleteBlocksByRecipeId(recipeId);

        if (recipe.getBlockList() != null && !recipe.getBlockList().isEmpty()) {

            for (int i = 0; i < recipe.getBlockList().size(); i++) {
                RecipeBlockVO block = recipe.getBlockList().get(i);
                block.setRecipeId(recipeId);
                block.setSortOrder(i + 1);

                recipeMapper.insertRecipeBlock(block);
            }
        }

        // -------------------------
        // 4. 재료 처리 (🔥 FIX)
        // -------------------------
        recipeMapper.deleteIngredients(recipeId);

        if (recipe.getIngredientList() != null && !recipe.getIngredientList().isEmpty()) {

            for (RecipeIngredientVO ingredient : recipe.getIngredientList()) {
                ingredient.setRecipeId(recipeId);
                recipeMapper.insertIngredient(ingredient);
            }
        }

        return 1;
    }
    
    @Override
    public int updateBlock(RecipeBlockVO block) {
        return recipeMapper.updateBlockContent(block);
    }

    // =========================
    // 📌 삭제
    // =========================
    @Override
    @Transactional
    public int deleteRecipe(Long recipeId) {
    	recipeMapper.deleteRecipeLike(recipeId);
    	recipeMapper.deleteRecipeBookmark(recipeId);
    	recipeMapper.deleteIngredients(recipeId);
    	recipeMapper.deleteBlocksByRecipeId(recipeId);

    	recipeMapper.deleteRecipe(recipeId);

        return 1;
    }

    // =========================
    // 📌 좋아요
    // =========================
    @Override
    @Transactional
    public boolean toggleLike(Long memberId, Long recipeId) {
    	
    	 System.out.println("SERVICE -- memberId = " + memberId);
    	 System.out.println("SERVICE -- recipeId = " + recipeId);

        int exists = recipeMapper.existsLike(memberId, recipeId);
        
        System.out.println("exists - " + exists);

        if (exists > 0) {
            recipeMapper.deleteLike(memberId, recipeId);
            return false;
        } else {
            recipeMapper.insertLike(memberId, recipeId);
            return true;
        }
    }

    // =========================
    // 📌 북마크
    // =========================
    @Override
    @Transactional
    public boolean toggleBookmark(Long memberId, Long recipeId) {

        int exists = recipeMapper.existsBookmark(memberId, recipeId);

        if (exists > 0) {
            recipeMapper.deleteBookmark(memberId, recipeId);
            return false;
        } else {
            recipeMapper.insertBookmark(memberId, recipeId);
            return true;
        }
    }

    // =========================
    // 📌 썸네일 추출
    // =========================
    private String extractThumbnail(List<RecipeBlockVO> blocks) {

        if (blocks == null || blocks.isEmpty()) {
            return "/images/default.png";
        }

        for (RecipeBlockVO block : blocks) {
            if ("IMAGE".equals(block.getBlockType())) {
                return block.getContent();
            }
        }

        return "/images/default.png";
    }
}