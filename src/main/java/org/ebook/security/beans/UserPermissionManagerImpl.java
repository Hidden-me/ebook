package org.ebook.security.beans;

import org.ebook.dao.UserManager;
import org.ebook.entity.User;
import org.ebook.security.UserPermissionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPermissionManagerImpl implements UserPermissionManager {
    private UserManager userManager;

    @Autowired
    public UserPermissionManagerImpl(UserManager userManager){
        this.userManager = userManager;
    }

    public boolean isAdmin(String username){
        User u = userManager.getUserByName(username);
        if(u == null){
            return false;
        }
        return u.isAdmin();
    }
    public boolean isBanned(String username){
        User u = userManager.getUserByName(username);
        if(u == null){
            return false;
        }
        return u.isBanned();
    }
}
