package com.example.weibo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class LoginController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @PostMapping("/register")
    public String register(@RequestParam("id") String id,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password){
        String sql = "insert into userinfo values (?,?,?);";
        jdbcTemplate.update(sql,Integer.parseInt(id),username,password);
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/login")
    public String login(@RequestParam("id") String id,
                        @RequestParam("password") String password,
                        HttpSession httpSession){
        String sql = "select * from userinfo where id=?;";
        try {
            Map<String, Object> map = jdbcTemplate.queryForMap(sql,Integer.parseInt(id));
            if(password.equals(map.get("password"))) {
                httpSession.setAttribute("logged", true);
                httpSession.setAttribute("id", id);
                httpSession.setAttribute("name",(String)map.get("name"));
                return "redirect:/home";
            }
            else
                return "redirect:/login";
        }
        catch (Exception e){
            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
