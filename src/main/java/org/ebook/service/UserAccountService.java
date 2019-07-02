package org.ebook.service;

import org.ebook.util.JSONResponse;

public interface UserAccountService {
    JSONResponse register(String username, String passToken, String email);
    JSONResponse getUserInfo(int uid);
    JSONResponse getUserList();
    JSONResponse switchBanState(int uid);
}
