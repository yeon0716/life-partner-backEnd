package com.life.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomPrincipal {
    private String email;
    private Long memberId;
}