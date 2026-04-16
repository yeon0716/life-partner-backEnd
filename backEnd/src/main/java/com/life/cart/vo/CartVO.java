package com.life.cart.vo;

import java.util.Date;

import lombok.Data;

@Data
public class CartVO {
    private Long cartId;
    private Long memberId;
    private Date createdAt;
}