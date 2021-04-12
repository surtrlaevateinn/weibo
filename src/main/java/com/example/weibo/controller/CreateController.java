package com.example.weibo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Controller
public class CreateController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/create")
    public String create(){
        return "create";
    }

    @PostMapping("/create")
    public String create(HttpServletRequest request,
                         @RequestParam("img") MultipartFile[] imgs,
                         HttpSession httpSession){
        String title = request.getParameter("title");
        String text = request.getParameter("text");
        String sql = "insert into articles (title,content,likes,comment,author) values (?,?,?,?,?);";
        jdbcTemplate.update(sql,title,text,0,0,Integer.parseInt((String)httpSession.getAttribute("id")));
        sql = "SELECT id FROM articles WHERE title=? AND content=?;";
        Map<String, Object> article_map = jdbcTemplate.queryForMap(sql,title,text);
        for(MultipartFile img:imgs){
            try {
                if(img.isEmpty()) System.out.println("nulllllllll");
                String code = Base64.getEncoder().encodeToString(img.getBytes());
                sql = "insert into imgs (art_id,img) values (?,?);";
                jdbcTemplate.update(sql,(int) article_map.get("id"),code);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/home";
    }
}
