package org.ebook.service;

public interface SessionService {
    String getUsername();
    String getIdentity();
    String getToken();
    void setUsername(String username);
    void setIdentity(String identity);
    void setToken(String token);
    void setLastReferencedBookIsbn(String isbn);
    String getLastReferencedBookIsbn();
}
