package com.life.tip.vo;

import java.util.Date;

import lombok.Data;

@Data
public class TipCommentVO {
    private Long commentId;
    private Long tipId;
    private Long memberId;
    private String content;
    private Date createdAt;
}
