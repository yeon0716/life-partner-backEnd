package com.life.account.vo;

import lombok.Data;

@Data
public class AccountCategoryVO {
	 private Long categoryId;
	 private Long memberId;

	 private String categoryName;
	 private String type; // INCOME, EXPENSE

	 private String createdAt;
}
