package org.ebook.security;

public interface UserPermissionManager {
    boolean isAdmin(String username);
    boolean isBanned(String username);
}
