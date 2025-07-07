package com.dunjia.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trade {
    private Integer id;
    private Integer buyerId;
    private Integer sellerId;
    private Integer productId;
    private String productName;
    private Double totalAmount;
    private Integer status;
    private LocalDateTime createTime;
}
