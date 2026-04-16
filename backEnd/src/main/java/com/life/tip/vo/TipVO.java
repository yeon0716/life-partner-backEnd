package com.life.tip.vo;

import java.util.Date;

import lombok.Data;

@Data
public class TipVO {
    private Long tipId;
    private Long memberId;
    private String title;
    private String content;
    private Date createdAt;
}