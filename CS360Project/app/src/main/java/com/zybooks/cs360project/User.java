package com.zybooks.cs360project;

public class User {
    private String username;
    private String password;
    private int smsBool;

    public User() {
        username = null;
        password = null;
        smsBool = 3;
    }

    public User(String newUsername) {
        username = newUsername;
        password = null;
    }

    public User(String newUsername, String newPassword) {
        username = newUsername;
        password = newPassword;
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

    public boolean getSmsBool() {
        switch(smsBool) {
            case 1:
                return true;
            default:
                return false;
        }
    }

    public void setSmsBool(boolean newSmsBool) { smsBool = (newSmsBool) ? 1 : 0; }
    public void setSmsBool(int newSmsBool) { smsBool = newSmsBool; }
    public boolean smsPermAlreadyAsked() {
        return (smsBool == 3) ? false : true;
    }
}
