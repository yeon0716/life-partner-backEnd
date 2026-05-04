package com.life.account.service;

import java.util.List;

import com.life.account.vo.AccountCategoryVO;

public interface AccountCategoryService {
	// 카테고리 등록
    int insert(AccountCategoryVO vo);

    // 전체 조회
    List<AccountCategoryVO> findAll(Long memberId);

    // 타입별 조회
    List<AccountCategoryVO> findByType(Long memberId, String type);

    // 삭제
    int delete(Long categoryId);
}
