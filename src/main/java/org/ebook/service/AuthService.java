package org.ebook.service;

import org.ebook.util.JSONResponse;

public interface AuthService {
    JSONResponse validate(String username, String token);
    JSONResponse getRandomPubkey(String username);
    JSONResponse getRefreshedAuthInfo(String username, String token);
    void exitLogin(String username);
}
