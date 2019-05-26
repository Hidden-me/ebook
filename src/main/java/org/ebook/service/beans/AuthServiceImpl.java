package org.ebook.service.beans;

import org.ebook.dao.UserManager;
import org.ebook.entity.User;
import org.ebook.security.UserValidator;
import org.ebook.service.AuthService;
import org.ebook.util.JSONResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    public JSONResponse validate(String username, String token){
        JSONResponse resp = new JSONResponse();
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
                resp.setMessage("该用户已被封禁，无法登录");
                resp.fail();
            }else{
                resp.succeed();
            }
        }else{
            resp.setMessage("登录信息有误");
            resp.fail();
        }
        return resp;
    }
    public JSONResponse getRandomPubkey(String username){
        JSONResponse resp = new JSONResponse();
        String key = "";
        if(username != null){
            key = UserValidator.getRandomPubkey(username);
        }
        resp.put("pubkey", key);
        resp.succeed();
        return resp;
    }
    public JSONResponse getRefreshedAuthInfo(String username, String token){
        JSONResponse resp = new JSONResponse();
        String identity = "visitor";
        if(username == null || token == null){
            username = "";
        }else{
            boolean succ = UserValidator.validate(username, token);
            if(succ){
                User user = UserManager.getUserByName(username);
                identity = user.isAdmin() ? "admin" : "user";
            }else{
                username = "";
            }
        }
        resp.put("username", username);
        resp.put("identity", identity);
        resp.succeed();
        return resp;
    }
    public void exitLogin(String username){
        if(username != null){
            // remove the user's public key
            UserValidator.removePubkey(username);
        }
    }
}
