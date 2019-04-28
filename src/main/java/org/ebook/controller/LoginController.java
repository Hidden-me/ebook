package org.ebook.controller;

import org.ebook.entity.User;
import org.ebook.entity.UserManager;
import org.ebook.security.UserValidator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public ModelAndView getLoginView(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }
    @PostMapping("pubkey")
    public String getRandomPubkey(@RequestBody Map<String, Object> req){
        String username = (String) req.get("username");
        String key = "";
        if(username != null){
            key = UserValidator.getRandomPubkey(username);
        }
        String result = "{\"pubkey\":\"" + key + "\"}";
        return result;
    }
    @PostMapping("validate")
    public String validate(@RequestBody Map<String, Object> req){
        String result = "{";
        String message = "";
        String username = (String) req.get("username");
        String token = (String) req.get("token");
        String identity = "user";
        boolean succ = false, banned = false;
        if(username != null && token != null){
            succ = UserValidator.validate(username, token);
            User user = UserManager.getUserByName(username);
            if(user != null){
                banned = user.isBanned();
                if(user.isAdmin()){
                    identity = "admin";
                }
            }
        }
        if(succ){
            if(banned){
                succ = false;
                message = "该用户已被封禁，无法登录";
            }else{
                HttpSession ss = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
                ss.setAttribute("username", username);
                ss.setAttribute("token", token);
                ss.setAttribute("identity", identity);
            }
        }else{
            message = "登录信息有误";
        }
        result += "\"result\":\"" + (succ ? "success" : "failure") + "\"," +
                "\"message\":\"" + message + "\"}";
        return result;
    }
}
