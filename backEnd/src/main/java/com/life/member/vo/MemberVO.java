package com.life.member.vo;

import java.util.Date;

import lombok.Data;

@Data
public class MemberVO {
    private Long memberId;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String gender;
    private Integer role;
    private String status;
    private String emailVerified;
    private Date createdAt;
}