package org.ebook.security;

public interface UserValidator {
    boolean validate(String username, String md5Token);
    void removePubkey(String username);
    String getRandomPubkey(String username);
}
