package com.life.cart.vo;

import java.util.Date;

import lombok.Data;

@Data
public class CartItemVO {
    private Long cartItemId;
    private Long cartId;
    private Long hotdealId;
    private Integer quantity;
    private Date addedAt;
}
