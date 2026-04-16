package com.life.member.dto;

import com.life.member.vo.MemberVO;

import lombok.Data;

@Data
public class LoginResponseDTO {

    private String token;
    private String email;
    private String name;
    private Integer role;

    public LoginResponseDTO(String token, MemberVO member) {
        this.token = token;
        this.email = member.getEmail();
        this.name = member.getName();
        this.role = member.getRole();
    }
}
