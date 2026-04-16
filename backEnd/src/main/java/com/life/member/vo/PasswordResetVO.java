package com.life.member.vo;

import java.util.Date;

import lombok.Data;

@Data
public class PasswordResetVO {
    private Long resetId;
    private String email;
    private String tempPassword;
    private Date expireTime;
    private String used;
}
