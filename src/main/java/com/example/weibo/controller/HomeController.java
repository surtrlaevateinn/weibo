package com.example.weibo.controller;

import com.example.weibo.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/home")
    public String home(Model model){
        String sql = "select id,title from articles;";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        model.addAttribute("articles",list);
        return "home";
    }
}
