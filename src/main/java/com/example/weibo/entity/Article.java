package com.example.weibo.entity;

import lombok.Data;

@Data
public class Article {
    private Integer id;
    private String title;
    private String content;
    private Integer likes;
    private Integer comment;
    private Integer author;
    private String time;
}
