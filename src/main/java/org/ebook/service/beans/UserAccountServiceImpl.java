package org.ebook.service.beans;

import org.ebook.dao.UserManager;
import org.ebook.entity.User;
import org.ebook.service.UserAccountService;
import org.ebook.util.DatabaseUtils;
import org.ebook.util.JSONResponse;
import org.springframework.stereotype.Service;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    public JSONResponse register(String username, String passToken, String email){
        JSONResponse resp = new JSONResponse();
        String token = "";
        if(username != null && passToken != null && email != null){
            // check if the user exists
            if(UserManager.getUserByName(username) != null){
                resp.setMessage("用户名已存在");
                resp.fail();
                return resp;
            }
            if(UserManager.getUserByEmail(email) != null){
                resp.setMessage("邮箱已被注册");
                resp.fail();
                return resp;
            }
            // save to DB
            User user = new User();
            user.setUsername(username);
            user.setPassToken(passToken);
            user.setEmail(email);
            if(!DatabaseUtils.save(user)){
                // DBMS failure
                resp.setMessage("存储注册信息失败，请重新注册");
                resp.fail();
                return resp;
            }
        }else{
            resp.setMessage("注册信息无效，请重新注册");
            resp.fail();
            return resp;
        }
        resp.succeed();
        return resp;
    }
}
