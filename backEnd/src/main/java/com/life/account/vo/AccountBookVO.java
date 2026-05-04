package com.life.account.vo;

import java.util.Date;

import lombok.Data;

@Data
public class AccountBookVO {
	private Long accountId;
    private Long memberId;
    private Long categoryId;

    private String categoryName; // JOIN용 (중요)

    private String title;
    private String content;

    private Integer amount;
    private String type; // INCOME, EXPENSE

    private String createdAt; // yyyy-MM-dd
}