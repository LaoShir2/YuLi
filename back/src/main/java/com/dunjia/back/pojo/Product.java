package com.dunjia.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private int id;
    private String name;
    private double price;
    private String image;
    private String description;
    private int address;
    private Integer buyerId;
    private Integer sellerId;
    private Integer condition; // 二手 -- 1 全新 -- 2
    private Integer status; // 待支付 -- 1 已支付 -- 2 已发货 -- 3 已完成 -- 4
    private LocalDateTime createTime; // 创建时间
    private Integer likes; // 想要人数
}
