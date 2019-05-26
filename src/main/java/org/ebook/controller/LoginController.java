package org.ebook.controller;

import org.ebook.service.AuthService;
import org.ebook.service.SessionService;
import org.ebook.util.JSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    private AuthService auth;
    private SessionService session;

    @Autowired
    public LoginController(AuthService auth, SessionService session){
        this.auth = auth;
        this.session = session;
    }

    @GetMapping
    public ModelAndView getLoginView(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }
    @PostMapping("pubkey")
    public Map<String, Object> getRandomPubkey(@RequestBody Map<String, Object> req){
        String username = (String) req.get("username");
        return auth.getRandomPubkey(username).getJSON();
    }
    @PostMapping("validate")
    public Map<String, Object> validate(@RequestBody Map<String, Object> req){
        String username = (String) req.get("username");
        String token = (String) req.get("token");
        JSONResponse resp = auth.validate(username, token);
        if(resp.isSuccessful()){
            session.setUsername(username);
            session.setToken(token);
        }
        return resp.getJSON();
    }
}
