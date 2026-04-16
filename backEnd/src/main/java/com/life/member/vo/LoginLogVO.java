package com.life.member.vo;

import java.util.Date;

import lombok.Data;

@Data
public class LoginLogVO {
    private Long logId;
    private Long memberId;
    private Date loginTime;
}