package org.ebook.controller;

import org.ebook.security.UserPermissionManager;
import org.ebook.security.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(getClass());
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
        logger.info("username: " + username);
        logger.info("token: " + token);
        boolean succ = false;
        if(username != null && token != null){
            succ = UserValidator.validate(username, token);
        }
        if(succ){
            HttpSession ss = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            ss.setAttribute("username", username);
            ss.setAttribute("token", token);
            logger.info("validation succeeded");
        }else{
            logger.info("validation failed");
        }
        result = "{\"result\":\"" + (succ ? "success" : "failure") + "\"}";
        return result;
    }
}
