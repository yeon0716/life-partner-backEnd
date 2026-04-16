package com.life.member.dto;

import lombok.Data;

@Data
public class EmailAuthDTO {
    private String email;
    private String authCode;
}
