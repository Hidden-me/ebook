package org.ebook.controller;

import org.ebook.service.AuthService;
import org.ebook.service.SessionService;
import org.ebook.util.JSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/identity")
public class IdentityController {
    private AuthService auth;
    private SessionService session;

    @Autowired
    public IdentityController(AuthService auth, SessionService session){
        this.auth = auth;
        this.session = session;
    }

    @PostMapping
    public Map<String, Object> getAuthInfo(){
        String username = session.getUsername();
        String token = session.getToken();
        JSONResponse resp = auth.getRefreshedAuthInfo(username, token);
        session.setIdentity((String) resp.get("identity"));
        return resp.getJSON();
    }
    @PostMapping("exit")
    public void exitLogin(){
        String username = session.getUsername();
        auth.exitLogin(username);
    }
}
