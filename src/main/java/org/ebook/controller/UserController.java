package org.ebook.controller;

import org.ebook.service.UserAccountService;
import org.ebook.util.JSONResponse;
import org.ebook.util.ParserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserAccountService userAccount;

    @Autowired
    public UserController(UserAccountService userAccount){
        this.userAccount = userAccount;
    }

    @GetMapping
    public String getUserView(){
        return "user test";
    }
    @PostMapping("list")
    public Map<String, Object> getUserList() {
        return userAccount.getUserList().getJSON();
    }
    @PostMapping("ban")
    public Map<String, Object> switchBanState(@RequestBody Map<String, Object> req){
        try{
            int uid = ParserUtils.parseInt(req.get("uid"));
            return userAccount.switchBanState(uid).getJSON();
        }catch(Exception e){
            e.printStackTrace();
            JSONResponse resp = new JSONResponse();
            resp.fail();
            return resp.getJSON();
        }
    }
    @PostMapping("refresh")
    public Map<String, Object> getUserInfo(@RequestBody Map<String, Object> req){
        try{
            int uid = ParserUtils.parseInt(req.get("uid"));
            return userAccount.getUserInfo(uid).getJSON();
        }catch(Exception e){
            e.printStackTrace();
            JSONResponse resp = new JSONResponse();
            resp.fail();
            return resp.getJSON();
        }
    }
}
