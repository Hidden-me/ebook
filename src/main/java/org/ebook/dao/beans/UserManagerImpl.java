package org.ebook.dao.beans;

import org.ebook.dao.UserManager;
import org.ebook.entity.User;
import org.ebook.util.DatabaseUtils;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class UserManagerImpl implements UserManager {
    public User getUserByUid(int uid){
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
    public User getUserByName(String username){
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
    public User getUserByEmail(String email){
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
    public Map<String, Object> getUserJSONByUid(int uid){
        User user = getUserByUid(uid);
        if(user == null){
            return null;
        }
        return user.getJSON();
    }
    public List<Object> getUserListJSON(){
        List<Object> list = new LinkedList<>();
        List<User> users = DatabaseUtils.createQuery(User.class).getResult();
        for(User user : users){
            list.add(user.getJSON());
        }
        return list;
    }
    public int getUid(String username){
        User user = getUserByName(username);
        if(user == null){
            return -1;
        }
        return user.getUid();
    }
}
