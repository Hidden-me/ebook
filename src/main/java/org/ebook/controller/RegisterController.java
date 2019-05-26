package org.ebook.controller;

import org.ebook.service.AuthService;
import org.ebook.service.SessionService;
import org.ebook.service.UserAccountService;
import org.ebook.util.JSONResponse;
import org.ebook.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
@RequestMapping("/register")
public class RegisterController {
    private AuthService auth;
    private UserAccountService account;
    private SessionService session;

    @Autowired
    public RegisterController(AuthService auth, UserAccountService account, SessionService session){
        this.auth = auth;
        this.account = account;
        this.session = session;
    }

    @GetMapping
    public ModelAndView getRegisterView(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }
    @PostMapping
    public Map<String, Object> registerUser(@RequestBody Map<String, Object> req){
        String username = (String) req.get("username");
        String passToken = (String) req.get("passToken");
        String email = (String) req.get("email");
        boolean autoLogin = (Boolean) req.get("autoLogin");
        JSONResponse resp = account.register(username, passToken, email);
        if(resp.isSuccessful() && autoLogin){
            // if auto-login is checked, set up the session directly
            String pubkey = (String) auth.getRandomPubkey(username).get("pubkey");
            String token = SecurityUtils.md5(pubkey + passToken);
            session.setUsername(username);
            session.setToken(token);
            session.setIdentity("user");
        }
        return resp.getJSON();
    }
}
