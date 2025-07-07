package com.dunjia.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Announcement {
    private int id;
    private String title;
    private String content;
    private LocalDateTime createTime;
}
