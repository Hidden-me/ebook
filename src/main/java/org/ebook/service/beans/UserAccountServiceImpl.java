package org.ebook.service.beans;

import org.ebook.dao.UserManager;
import org.ebook.entity.User;
import org.ebook.service.UserAccountService;
import org.ebook.util.DatabaseUtils;
import org.ebook.util.JSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    private UserManager userManager;

    @Autowired
    public UserAccountServiceImpl(UserManager userManager){
        this.userManager = userManager;
    }

    public JSONResponse register(String username, String passToken, String email){
        JSONResponse resp = new JSONResponse();
        String token = "";
        if(username != null && passToken != null && email != null){
            // check if the user exists
            if(userManager.getUserByName(username) != null){
                resp.setMessage("用户名已存在");
                resp.fail();
                return resp;
            }
            if(userManager.getUserByEmail(email) != null){
                resp.setMessage("邮箱已被注册");
                resp.fail();
                return resp;
            }
            // insert to DB
            User user = new User();
            user.setUsername(username);
            user.setPassToken(passToken);
            user.setEmail(email);
            if(!DatabaseUtils.insert(user)){
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

    public JSONResponse getUserInfo(int uid){
        JSONResponse resp = new JSONResponse();
        Map<String, Object> json = userManager.getUserJSONByUid(uid);
        if(json == null){
            resp.fail();
        }else{
            resp.put("user", json);
            resp.succeed();
        }
        return resp;
    }

    public JSONResponse getUserList(){
        JSONResponse resp = new JSONResponse();
        resp.put("users", userManager.getUserListJSON());
        resp.succeed();
        return resp;
    }

    public JSONResponse switchBanState(int uid){
        JSONResponse resp = new JSONResponse();
        User user = userManager.getUserByUid(uid);
        if(user == null){
            resp.setMessage("用户不存在");
            resp.fail();
        }else{
            user.setBanned(!user.isBanned());
            boolean succ = DatabaseUtils.update(user);
            if(succ){
                resp.succeed();
            }else{
                resp.fail();
            }
        }
        return resp;
    }
}
