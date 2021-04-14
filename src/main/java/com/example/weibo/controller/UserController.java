package com.example.weibo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/myArticle")
    public String myArticle(Model model, HttpSession httpSession){
        String sql = "select id,title from articles WHERE author = ?;";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,Integer.parseInt((String) httpSession.getAttribute("id")));
        model.addAttribute("articles",list);
        return "myArticle";
    }

    @GetMapping("/article/update/{id}")
    public String updateArticle(@PathVariable String id,
                                Model model){
        String sql = "SELECT * FROM articles WHERE id = ?;";
        Map<String, Object> article_map = jdbcTemplate.queryForMap(sql,Integer.parseInt(id));
        model.addAttribute("article",article_map);
        return "update";
    }

    @PostMapping("/article/update/{id}")
    public String updateArticle(@PathVariable String id,
                                @RequestParam("title")String title,
                                @RequestParam("text")String text,
                                HttpSession httpSession){
        String sql1 = "UPDATE articles SET title=?,content=? WHERE id=?;";
        jdbcTemplate.update(sql1,title,text,Integer.parseInt(id));

        String sql2 = "INSERT INTO updates (user_id,art_id) VALUES (?,?);";
        jdbcTemplate.update(sql2,Integer.parseInt((String)httpSession.getAttribute("id")),Integer.parseInt(id));
        return "redirect:/myArticle";
    }

    @PostMapping("/delete")
    @ResponseBody
    public String deleteArticle(@RequestParam("id")String id,
                                @RequestParam("title")String title,
                                HttpSession httpSession){
        String sql1 = "INSERT INTO deletes (user_id,art_id,title) VALUES (?,?,?);";
        jdbcTemplate.update(sql1,Integer.parseInt((String)httpSession.getAttribute("id")),Integer.parseInt(id),title);

        String sql2 = "DELETE FROM comments WHERE art_id=?";
        jdbcTemplate.update(sql2,Integer.parseInt(id));
        sql2 = "DELETE FROM updates WHERE art_id=?";
        jdbcTemplate.update(sql2,Integer.parseInt(id));
        sql2 = "DELETE FROM likes WHERE art_id=?";
        jdbcTemplate.update(sql2,Integer.parseInt(id));
        sql2 = "DELETE FROM articles WHERE id=?";
        jdbcTemplate.update(sql2,Integer.parseInt(id));

        return "succeed";
    }
}
