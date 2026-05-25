package com.life.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.life.account.vo.AccountCategoryVO;

@Mapper
public interface AccountCategoryMapper {
	 // 카테고리 등록
    int insert(AccountCategoryVO vo);

    // 카테고리 전체 조회
    List<AccountCategoryVO> findAll(Long memberId);

    // 타입별 조회
    List<AccountCategoryVO> findByType(Long memberId, String type);

    // 삭제
    int delete(Long categoryId);
}
