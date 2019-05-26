package org.ebook.dao;

import org.ebook.entity.User;
import org.ebook.util.DatabaseUtils;

import java.util.*;

public class UserManager {
    public static User getUserByUid(int uid){
        List<User> users = DatabaseUtils.createQuery(User.class)
                .equal("uid", uid)
                .getResult();
        if(users == null){
            return null;
        }else{
            if(users.isEmpty()){
                return null;
            }
            return users.get(0);
        }
    }
    public static User getUserByName(String username){
        List<User> users = DatabaseUtils.createQuery(User.class)
                .equal("username", username)
                .getResult();
        if(users == null){
            return null;
        }else{
            if(users.isEmpty()){
                return null;
            }
            return users.get(0);
        }
    }
    public static User getUserByEmail(String email){
        List<User> users = DatabaseUtils.createQuery(User.class)
                .equal("email", email)
                .getResult();
        if(users == null){
            return null;
        }else{
            if(users.isEmpty()){
                return null;
            }
            return users.get(0);
        }
    }
    public static int getUid(String username){
        User user = getUserByName(username);
        if(user == null){
            return -1;
        }
        return user.getUid();
    }
}
