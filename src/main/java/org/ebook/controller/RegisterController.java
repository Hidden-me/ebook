package org.ebook.controller;

import org.ebook.DatabaseUtils;
import org.ebook.entity.User;
import org.ebook.entity.UserManager;
import org.ebook.security.SecurityUtils;
import org.ebook.security.UserValidator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @GetMapping
    public ModelAndView getLoginView(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }
    @PostMapping
    public String registerUser(@RequestBody Map<String, Object> req){
        String result = "{";
        String username = (String) req.get("username");
        String passToken = (String) req.get("passToken");
        String email = (String) req.get("email");
        boolean autoLogin = (Boolean) req.get("autoLogin");
        String token = "";
        if(username != null && passToken != null && email != null){
            // check if the user exists
            if(UserManager.getUserByName(username) != null){
                return "{\"result\":\"failure\"," +
                        "\"message\":\"用户名已存在\"}";
            }
            if(UserManager.getUserByEmail(email) != null){
                return "{\"result\":\"failure\"," +
                        "\"message\":\"邮箱已被注册\"}";
            }
            // save to DBMS
            User user = new User();
            user.setUsername(username);
            user.setPassToken(passToken);
            user.setEmail(email);
            if(!DatabaseUtils.save(user)){
                // DBMS failure
                return "{\"result\":\"failure\"}" +
                        "\"message\":\"存储注册信息失败\"}";
            }
            if(autoLogin){
                // if auto-login is checked, set up the session directly
                String pubkey = UserValidator.getRandomPubkey(username);
                token = SecurityUtils.md5(pubkey + passToken);
                HttpSession ss = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
                ss.setAttribute("username", username);
                ss.setAttribute("token", token);
            }
        }
        result += "\"username\":\"" + (username == null ? "" : username) + "\",";
        result += "\"token\":\"" + token + "\",";
        result += "\"result\":\"success\"";
        result += "}";
        return result;
    }
}
