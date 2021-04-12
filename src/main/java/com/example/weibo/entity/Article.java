package com.example.weibo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Article {
    private Integer id;
    private String title;
    private String content;
    private Integer likes;
    private Integer comment;
    private Integer author;
    private String time;

}
