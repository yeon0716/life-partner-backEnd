package com.life.recipe.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.life.member.mapper.MemberMapper;
import com.life.member.vo.MemberVO;
import com.life.recipe.service.RecipeService;
import com.life.recipe.vo.RecipeBlockVO;
import com.life.recipe.vo.RecipeCategoryVO;
import com.life.recipe.vo.RecipeVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final MemberMapper memberMapper;
    
    // ✅ 1. 리스트 조회
    @GetMapping
    public ResponseEntity<List<RecipeVO>> getRecipeList(
    		@RequestParam(required = false) String keyword,
    		@RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(recipeService.getRecipeList(keyword, categoryId, page, size));
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<RecipeCategoryVO>> getCategories() {
        return ResponseEntity.ok(recipeService.getCategoryList());
    }

    // ✅ 2. 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<RecipeVO> getRecipeDetail(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getRecipeDetail(id));
    }

    // ✅ 3. 레시피 등록
    @PostMapping
    public ResponseEntity<Long> createRecipe(@RequestBody RecipeVO vo) {
    	String email =
    		    SecurityContextHolder.getContext()
    		    .getAuthentication()
    		    .getName();

    		MemberVO member = memberMapper.selectByEmail(email);

    		vo.setMemberId(member.getMemberId());
        recipeService.createRecipe(vo);
        return ResponseEntity.ok(vo.getRecipeId());
    }

    // ✅ 4. 레시피 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable Long id,
                                          @RequestBody RecipeVO recipe) {
        recipe.setRecipeId(id);
        recipeService.updateRecipe(recipe);
        return ResponseEntity.ok("success");
    }

    // ✅ 5. 레시피 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.ok("success");
    }

    // ✅ 7. 블록 수정 (이미지 변경 포함)
    @PutMapping("/block")
    public ResponseEntity<?> updateBlock(@RequestBody RecipeBlockVO block) {
        recipeService.updateBlock(block);
        return ResponseEntity.ok("success");
    }

    // ===============================
    // 8. 이미지 업로드 (Multipart)
    // ===============================
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException("파일 없음");
        }

        String originalName = file.getOriginalFilename();

        String fileName = UUID.randomUUID() + "_" + originalName;

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
    
    // 북마크 등록&삭제    
    @PostMapping("/{id}/bookmark")
    public ResponseEntity<?> toggleBookmark(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName(); // ✅ 이걸로 바꿔

        Long memberId = memberMapper.selectByEmail(email).getMemberId();
        
        System.out.println("memberId = " + memberId);
        System.out.println("recipeId = " + id);

        recipeService.toggleBookmark(id, memberId);

        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/like")
    public boolean toggleLike(@PathVariable Long id, Authentication authentication) {
        Long memberId = (Long) authentication.getPrincipal();

        return recipeService.toggleLike(memberId, id);
    }
    
    
 
}
