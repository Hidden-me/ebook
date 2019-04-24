package org.ebook.controller;

import org.ebook.entity.User;
import org.ebook.entity.UserManager;
import org.ebook.security.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/identity")
public class IdentityController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @PostMapping
    public String getIdentity(){
        String result = "{";
        String identity = "";
        String username = "";
        HttpSession ss = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(false);
        if(ss == null){
            // non-existing session, visitor
            identity = "visitor";
        }else{
            username = (String) ss.getAttribute("username");
            String token = (String) ss.getAttribute("token");
            if(username == null || token == null){
                identity = "visitor";
                username = "";
            }else{
                boolean succ = UserValidator.validate(username, token);
                if(succ){
                    User user = UserManager.getUserByName(username);
                    identity = (user.isAdmin() ? "admin" : "user");
                }else{
                    identity = "visitor";
                    username = "";
                }
            }
        }
        result += "\"username\":\"" + username + "\",";
        result += "\"identity\":\"" + identity + "\"}";
        logger.info(result);
        return result;
    }
    @PostMapping("exit")
    public void exitLogin(@RequestBody Map<String, Object> req){
        String username = (String) req.get("username");
        if(username != null){
            // remove the user's public key
            UserValidator.removePubkey(username);
        }
    }
}
