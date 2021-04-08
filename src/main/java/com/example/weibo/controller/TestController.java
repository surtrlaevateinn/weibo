package com.example.weibo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class TestController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @PostMapping("/register")
    public String register(@RequestParam("id") String id,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password){
        String sql = "insert into userinfo values (?,?,?);";
        jdbcTemplate.update(sql,Integer.parseInt(id),username,password);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("id") String id,
                        @RequestParam("password") String password){
        String sql = "select * from userinfo where id=?;";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        if(password.equals(map.get("password")))
            return "home";
        else
            return "login";
    }
}
