package com.life.recipe.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.life.member.mapper.MemberMapper;
import com.life.member.vo.MemberVO;
import com.life.recipe.service.RecipeService;
import com.life.recipe.vo.RecipeBlockVO;
import com.life.recipe.vo.RecipeCategoryVO;
import com.life.recipe.vo.RecipeVO;
import com.life.util.CustomPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final MemberMapper memberMapper;

    /* =========================
       1. 리스트 (비로그인 OK)
    ========================= */
    @GetMapping
    public List<RecipeVO> getRecipeList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {

        Long memberId = null;

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {

            String email = authentication.getName();
            MemberVO member = memberMapper.selectByEmail(email);
            if (member != null) {
                memberId = member.getMemberId();
            }
        }

        return recipeService.getRecipeList(memberId, keyword, categoryId, page, size);
    }

    /* =========================
       2. 상세 (비로그인 OK)
    ========================= */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeVO> getRecipeDetail(
            @PathVariable Long id,
            Authentication authentication
    ) {

        Long memberId = null;

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {

            String email = authentication.getName();
            MemberVO member = memberMapper.selectByEmail(email);
            if (member != null) {
                memberId = member.getMemberId();
            }
        }

        return ResponseEntity.ok(recipeService.getRecipeDetail(memberId, id));
    }

    /* =========================
       3. 생성 (로그인 필수)
    ========================= */
    @PostMapping("/edit")
    public ResponseEntity<Long> createRecipe(
            @RequestBody RecipeVO vo,
            @AuthenticationPrincipal CustomPrincipal principal
    ) {

        if (principal == null) {
            throw new RuntimeException("로그인 필요");
        }

        vo.setMemberId(principal.getMemberId());

        recipeService.createRecipe(vo);

        return ResponseEntity.ok(vo.getRecipeId());
    }

    /* =========================
       4. 수정 (로그인 필수)
    ========================= */
    @PutMapping("/edit/{id}")
    public ResponseEntity<?> updateRecipe(
            @PathVariable Long id,
            @RequestBody RecipeVO recipe,
            @AuthenticationPrincipal CustomPrincipal principal
    ) {

        if (principal == null) {
            throw new RuntimeException("로그인 필요");
        }

        recipe.setRecipeId(id);
        recipe.setMemberId(principal.getMemberId());

        recipeService.updateRecipe(recipe);

        return ResponseEntity.ok("success");
    }

    /* =========================
       5. 삭제 (로그인 필수)
    ========================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomPrincipal principal
    ) {

        if (principal == null) {
            throw new RuntimeException("로그인 필요");
        }

        recipeService.deleteRecipe(id);

        return ResponseEntity.ok("success");
    }

    /* =========================
       6. 카테고리
    ========================= */
    @GetMapping("/categories")
    public ResponseEntity<List<RecipeCategoryVO>> getCategories() {
        return ResponseEntity.ok(recipeService.getCategoryList());
    }

    /* =========================
       7. 블록 수정
    ========================= */
    @PutMapping("/block")
    public ResponseEntity<?> updateBlock(@RequestBody RecipeBlockVO block) {
        recipeService.updateBlock(block);
        return ResponseEntity.ok("success");
    }

    /* =========================
       8. 이미지 업로드
    ========================= */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException("파일 없음");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        String uploadDir = "C:/upload/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        try {
            file.transferTo(new File(uploadDir + fileName));
        } catch (IOException e) {
            throw new RuntimeException("업로드 실패");
        }

        return ResponseEntity.ok("/images/" + fileName);
    }

    /* =========================
       9. LIKE (로그인 필수)
    ========================= */
    @PostMapping("/{id}/like")
    public ResponseEntity<?> toggleLike(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomPrincipal principal
    ) {

        if (principal == null) {
            throw new RuntimeException("로그인 필요");
        }

        recipeService.toggleLike(principal.getMemberId(), id);

        return ResponseEntity.ok().build();
    }

    /* =========================
       10. BOOKMARK (로그인 필수)
    ========================= */
    @PostMapping("/{id}/bookmark")
    public ResponseEntity<?> toggleBookmark(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomPrincipal principal
    ) {

        if (principal == null) {
            throw new RuntimeException("로그인 필요");
        }

        recipeService.toggleBookmark(id, principal.getMemberId());

        return ResponseEntity.ok().build();
    }
}