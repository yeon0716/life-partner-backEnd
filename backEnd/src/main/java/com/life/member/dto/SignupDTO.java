package com.life.member.dto;

import lombok.Data;

@Data
public class SignupDTO {
    private String email;
    private String password;
    private String name;
    private String phone;
}