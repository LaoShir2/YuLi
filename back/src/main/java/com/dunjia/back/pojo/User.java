package com.dunjia.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String password;
    private String email;
    private String wantList; // 存放用户想要的商品的id
//    private String phoneNumber;
    private String picture; // 头像
//    private String intro; // 个人简介
//    private String address;
//    private Integer numTransactions; // 交易数,上架数...
}
