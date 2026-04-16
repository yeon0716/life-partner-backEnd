package com.life.member.vo;

import java.util.Date;

import lombok.Data;

@Data
public class EmailAuthVO {
    private Long authId;
    private String email;
    private String authCode;
    private Date expireTime;
    private String verified;
}
