package com.life.account.vo;

import java.util.Date;

import lombok.Data;

@Data
public class AccountBookVO {
    private Long accountId;
    private Long memberId;
    private String category;
    private String title;
    private String content;
    private Integer amount;
    private String type; // INCOME / EXPENSE
    private Date createdAt;
}