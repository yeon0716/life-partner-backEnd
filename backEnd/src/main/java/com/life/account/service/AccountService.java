package com.life.account.service;

import java.util.List;
import java.util.Map;

import com.life.account.dto.CategoryRatioDTO;
import com.life.account.dto.MonthComparisonDTO;
import com.life.account.dto.MonthlySummaryDTO;
import com.life.account.vo.AccountBookVO;

public interface AccountService {

    // 등록
    int insert(AccountBookVO vo);

    // 전체 조회
    List<AccountBookVO> findAll(Long memberId);

    // 오늘
    List<AccountBookVO> findToday(Long memberId);

    // 월별
    List<AccountBookVO> findByMonth(Long memberId, String month);

    // 📅 캘린더 (DTO 추천)
    Map<String, Object> getCalendar(Long memberId, String month);

    // 💰 월 요약
    MonthlySummaryDTO getMonthlySummary(Long memberId, String month);

    // 🥧 카테고리 차트
    List<CategoryRatioDTO> getCategoryRatio(Long memberId, String month);

    // 삭제
    int delete(Long accountId);
    
    MonthComparisonDTO getMonthComparison(Long memberId, String month);
    
    
}