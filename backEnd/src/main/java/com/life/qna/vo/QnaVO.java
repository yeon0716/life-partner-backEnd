package com.life.qna.vo;

import java.util.Date;

import lombok.Data;

@Data
public class QnaVO {
    private Long qnaId;
    private Long memberId;
    private String title;
    private String content;
    private String answer;
    private String status;
    private Date createdAt;
}