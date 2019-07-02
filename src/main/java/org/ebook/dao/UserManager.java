package org.ebook.dao;

import org.ebook.entity.User;

import java.util.List;
import java.util.Map;

public interface UserManager {
    User getUserByUid(int uid);
    User getUserByName(String username);
    User getUserByEmail(String email);
    Map<String, Object> getUserJSONByUid(int uid);
    List<Object> getUserListJSON();
    int getUid(String username);
}
