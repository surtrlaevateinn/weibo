package com.example.weibo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class ArticleController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/article/{id}")
    public String article(@PathVariable String id,
                          Model model) throws SQLException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String sql1 = "SELECT * FROM articles WHERE id = ?;";
        String sql2 = "SELECT name FROM userinfo WHERE id = ?;";
        Map<String, Object> article_map = jdbcTemplate.queryForMap(sql1,Integer.parseInt(id));
        article_map.replace("time",df.format((LocalDateTime)article_map.get("time")));
        Map<String, Object> user_map = jdbcTemplate.queryForMap(sql2,article_map.get("author"));

        String sql3 = "SELECT comments.id,likes,content,name,time FROM comments,userinfo where userinfo.id=comments.user_id AND art_id=?;";
        List<Map<String,Object>> comment_list = jdbcTemplate.queryForList(sql3,Integer.parseInt(id));
        model.addAttribute("comments",comment_list);
        model.addAttribute("article", article_map);
        model.addAttribute("user", user_map);

        sql1 = "SELECT img FROM imgs WHERE art_id = ?;";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql1,Integer.parseInt(id));
        List<Map<String, String>> img_list = new ArrayList<>();
        try {
            for (Map<String, Object> i : list) {
                Map<String, String> map = new HashMap<>();
                map.put("img", new String((byte[]) i.get("img"), "UTF-8"));
                img_list.add(map);
            }
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        model.addAttribute("images",img_list);
        return "article";
    }

    @PostMapping("/article/like")
    @ResponseBody
    public String article_like(@RequestParam("id")String id,
                               HttpSession httpSession){
        String sql1 = "insert into likes (user_id,art_id) values (?,?);";
        String sql2 = "select likes from articles where id=?;";
        String sql3 = "update articles set likes=? where id=?;";
        Map<String, Object> article_map = jdbcTemplate.queryForMap(sql2,Integer.parseInt(id));
        Integer likes = (Integer)article_map.get("likes")+1;
        jdbcTemplate.update(sql1,Integer.parseInt((String) httpSession.getAttribute("id")),Integer.parseInt(id));
        jdbcTemplate.update(sql3,likes,Integer.parseInt(id));
        return "点赞数：" + likes.toString();
    }

    @GetMapping("/article/comment")
    public String article_comment(){
        return "comment";
    }

    @PostMapping("/article/comment")
    public String article_comment(@RequestParam("id")String id,
                                  @RequestParam("text")String text,
                                  HttpSession httpSession){
        String sql1 = "insert into comments (user_id,art_id,likes,content) values (?,?,?,?);";
        String sql2 = "update articles set comment=(select a.comment from(select comment from articles where id=?) a)+1 where id=?;";
        jdbcTemplate.update(sql1,Integer.parseInt((String) httpSession.getAttribute("id")),Integer.parseInt(id),0,text);
        jdbcTemplate.update(sql2,id,id);
        return "redirect:/article/" + id;
    }

    @PostMapping("/article/comment/like")
    @ResponseBody
    public String article_comment_like(@RequestParam("id")String id){
        String sql1 = "select likes from comments where id=?;";
        Map<String, Object> comment_map = jdbcTemplate.queryForMap(sql1,Integer.parseInt(id));
        Integer likes = (Integer)comment_map.get("likes")+1;
        String sql2 = "update comments set likes=? where id=?;";
        jdbcTemplate.update(sql2,likes,Integer.parseInt(id));
        return "点赞数：" + likes.toString();
    }
}
