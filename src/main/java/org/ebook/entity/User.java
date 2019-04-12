package org.ebook.entity;

public class User {
    private int uid;
    private String username;
    private String password;
    private boolean banned;
    private boolean online;
    private int currentCartId;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getCurrentCartId() {
        return currentCartId;
    }

    public void setCurrentCartId(int currentCartId) {
        this.currentCartId = currentCartId;
    }

    public User() {
    }

}
