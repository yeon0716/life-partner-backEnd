package com.life.hotdeal.vo;

import java.util.Date;

import lombok.Data;

@Data
public class HotdealVO {
    private Long hotdealId;
    private Long categoryId;
    private String title;
    private String url;
    private Integer price;
    private String imageUrl;
    private String status;
    private Integer discountRate;
    private Integer originalPrice;
    private Date createdAt;
}
