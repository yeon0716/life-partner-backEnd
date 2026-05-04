package com.life.account.dto;

import java.util.List;

import com.life.account.vo.AccountBookVO;

import lombok.Data;

@Data
public class CalenderDTO {
	
	 private String date; // 2026-04-01

	 private int incomeTotal;
	 private int expenseTotal;

	 private List<AccountBookVO> items;
}
