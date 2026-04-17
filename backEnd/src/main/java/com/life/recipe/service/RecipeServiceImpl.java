package com.life.recipe.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.life.recipe.mapper.RecipeMapper;
import com.life.recipe.vo.RecipeBlockVO;
import com.life.recipe.vo.RecipeVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeMapper recipeMapper;

    // 목록
    @Override
    public List<RecipeVO> getRecipeList(String keyword, int page, int size) {
        int start = (page - 1) * size + 1;
        int end = page * size;
        return recipeMapper.selectRecipeList(keyword, start, end);
    }

    // 상세
    @Override
    public RecipeVO getRecipeDetail(Long recipeId) {
        return recipeMapper.selectRecipeDetail(recipeId);
    }

    // 등록 (핵심)
    @Override
    @Transactional
    public int createRecipe(RecipeVO recipe) {

        // 1. 썸네일 자동 추출
        String thumbnail = extractThumbnail(recipe.getBlockList());
        recipe.setThumbnailUrl(thumbnail);

        // 2. recipe insert
        int result = recipeMapper.insertRecipe(recipe);

        if (result != 1) {
            throw new RuntimeException("RECIPE INSERT FAILED");
        }

        Long recipeId = recipe.getRecipeId();

        // 3. block insert (각각 안전 처리)
        if (recipe.getBlockList() != null && !recipe.getBlockList().isEmpty()) {

            for (RecipeBlockVO block : recipe.getBlockList()) {

                block.setRecipeId(recipeId);

                int r = recipeMapper.insertRecipeBlock(block);

                if (r != 1) {
                    throw new RuntimeException("BLOCK INSERT FAILED");
                }
            }
        }

        return 1;
    }

    // 수정
    @Override
    @Transactional
    public int updateRecipe(RecipeVO recipe) {

        // 1. 썸네일 재계산
        String thumbnail = extractThumbnail(recipe.getBlockList());
        recipe.setThumbnailUrl(thumbnail);

        // 2. recipe update
        int result = recipeMapper.updateRecipe(recipe);

        if (result != 1) {
            throw new RuntimeException("RECIPE UPDATE FAILED");
        }

        Long recipeId = recipe.getRecipeId();

        // 3. 기존 블록 삭제
        recipeMapper.deleteBlocksByRecipeId(recipeId);

        // 4. 새 블록 insert
        if (recipe.getBlockList() != null && !recipe.getBlockList().isEmpty()) {

            for (RecipeBlockVO block : recipe.getBlockList()) {

                block.setRecipeId(recipeId);

                int r = recipeMapper.insertRecipeBlock(block);

                if (r != 1) {
                    throw new RuntimeException("BLOCK UPDATE INSERT FAILED");
                }
            }
        }

        return 1;
    }

    // ======================
    // 📌 삭제
    // ======================
    @Override
    @Transactional
    public int deleteRecipe(Long recipeId) {

        recipeMapper.deleteBlocksByRecipeId(recipeId);

        int result = recipeMapper.deleteRecipe(recipeId);

        if (result != 1) {
            throw new RuntimeException("RECIPE DELETE FAILED");
        }

        return 1;
    }

    // ======================
    // 📌 단일 블록 수정
    // ======================
    @Override
    public int updateBlock(RecipeBlockVO block) {
        return recipeMapper.updateBlockContent(block);
    }

    // ======================
    // 📌 썸네일 추출
    // ======================
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