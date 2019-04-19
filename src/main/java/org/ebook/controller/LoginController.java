package org.ebook.controller;

import org.ebook.security.UserValidator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
        String result = "";
        String username = (String) req.get("username");
        String token = (String) req.get("token");
        boolean succ = false;
        if(username != null && token != null){
            succ = UserValidator.validate(username, token);
        }
        result = "{\"result\":\"" + (succ ? "success" : "failure") + "\"}";
        return result;
    }
}
