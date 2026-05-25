package com.life.account.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.life.account.mapper.AccountCategoryMapper;
import com.life.account.mapper.AccountMapper;
import com.life.account.vo.AccountCategoryVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountCategoryServiceImpl implements AccountCategoryService{
	
	private final AccountCategoryMapper categoryMapper;

	@Override
	public int insert(AccountCategoryVO vo) {
		// TODO Auto-generated method stub
		return categoryMapper.insert(vo);
	}

	@Override
	public List<AccountCategoryVO> findAll(Long memberId) {
		
		return categoryMapper.findAll(memberId);
	}

	@Override
	public List<AccountCategoryVO> findByType(Long memberId, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Long categoryId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
