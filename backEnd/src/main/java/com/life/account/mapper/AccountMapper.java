package com.life.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.life.account.dto.CategoryDeltaDTO;
import com.life.account.dto.CategoryRatioDTO;
import com.life.account.vo.AccountBookVO;

@Mapper
public interface AccountMapper {
	// 등록
    int insert(AccountBookVO vo);

    // 전체 조회
    List<AccountBookVO> findAll(Long memberId);

    // 오늘
    List<AccountBookVO> findToday(Long memberId);

    // 월별
    List<AccountBookVO> findByMonth(
            @Param("memberId") Long memberId,
            @Param("month") String month
        );

    // 삭제
    int delete(Long accountId);

    // 카테고리별 합계 (차트용)
    List<CategoryRatioDTO> categorySum(
            @Param("memberId") Long memberId,
            @Param("month") String month
        );
    // 해당 달 수입 합계
    int sumIncome(
            @Param("memberId") Long memberId,
            @Param("month") String month
        );
    // 해당 달 지출 합계
    int sumExpense(
            @Param("memberId") Long memberId,
            @Param("month") String month
        );
    
    // 이번달 카테고리
    List<CategoryDeltaDTO> currentCategorySum(
        @Param("memberId") Long memberId,
        @Param("month") String month
    );

    // 저번달 카테고리
    List<CategoryDeltaDTO> prevCategorySum(
        @Param("memberId") Long memberId,
        @Param("prevMonth") String prevMonth
    );
    
}
