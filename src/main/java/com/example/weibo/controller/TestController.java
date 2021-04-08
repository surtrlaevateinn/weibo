package com.example.weibo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TestController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @RequestMapping("/getUsers")
    public List<Map<String,Object>> getDbType(){
        String sql = "select * from userinfo";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }
}
