package com.life.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.life.account.service.AccountCategoryService;
import com.life.account.service.AccountService;
import com.life.account.vo.AccountBookVO;
import com.life.account.vo.AccountCategoryVO;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountCategoryService accountCategoryService;

    // =========================
    // ✅ 등록
    // =========================
    @PostMapping
    public ResponseEntity<?> insert(@RequestBody AccountBookVO vo) {
        return ResponseEntity.ok(accountService.insert(vo));
    }

    // =========================
    // ✅ 전체 조회
    // =========================
    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam Long memberId) {
        return ResponseEntity.ok(accountService.findAll(memberId));
    }

    // =========================
    // ✅ 오늘
    // =========================
    @GetMapping("/today")
    public ResponseEntity<?> today(@RequestParam Long memberId) {
        return ResponseEntity.ok(accountService.findToday(memberId));
    }

    // =========================
    // ✅ 월별 조회
    // =========================
    @GetMapping("/month")
    public ResponseEntity<?> month(
            @RequestParam Long memberId,
            @RequestParam String month // ex: 2026-04
    ) {
        return ResponseEntity.ok(
                accountService.findByMonth(memberId, month)
        );
    }

    // =========================
    // 📅 캘린더
    // =========================
    @GetMapping("/calendar")
    public ResponseEntity<?> calendar(
            @RequestParam Long memberId,
            @RequestParam String month
    ) {
    	System.out.println("Data memberId - " + memberId + " month = " + month);
        return ResponseEntity.ok(
                accountService.getCalendar(memberId, month)
        );
    }

    // =========================
    // 💰 월 요약
    // =========================
    @GetMapping("/summary")
    public ResponseEntity<?> summary(
            @RequestParam Long memberId,
            @RequestParam String month
    ) {
        return ResponseEntity.ok(
                accountService.getMonthlySummary(memberId, month)
        );
    }

    // =========================
    // 🥧 카테고리 차트
    // =========================
    @GetMapping("/category")
    public ResponseEntity<?> category(
            @RequestParam Long memberId,
            @RequestParam String month
    ) {
        return ResponseEntity.ok(
                accountService.getCategoryRatio(memberId, month)
        );
    }

    // =========================
    // ❌ 삭제
    // =========================
    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> delete(@PathVariable Long accountId) {
        return ResponseEntity.ok(
                accountService.delete(accountId)
        );
    }
    
    @GetMapping("/compare")
    public ResponseEntity<?> compare(Long memberId, String month) {
        return ResponseEntity.ok(accountService.getMonthComparison(memberId, month));
    }
    
    /* 
     * 
     * 카테고리 영역
     *  
    */
    
    // 리스트
    @GetMapping("/categoryList")
    public ResponseEntity<?> getCategoryList(Long memberId) {
        return ResponseEntity.ok(accountCategoryService.findAll(memberId));
    }
    
    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@RequestBody AccountCategoryVO vo) {
        return ResponseEntity.ok(accountCategoryService.insert(vo));
    
    }
  
}