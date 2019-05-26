package org.ebook.security;

import org.ebook.dao.UserManager;
import org.ebook.entity.User;

public class UserPermissionManager {
    public static boolean isAdmin(String username){
        User u = UserManager.getUserByName(username);
        if(u == null){
            return false;
        }
        return u.isAdmin();
    }
    public static boolean isBanned(String username){
        User u = UserManager.getUserByName(username);
        if(u == null){
            return false;
        }
        return u.isBanned();
    }
}
