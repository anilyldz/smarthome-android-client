package com.anil.akilhane;

/**
 * Created by Anil on 23.04.2016.
 */
public class User {

    private static User instance = new User();

    private User() {
    }

    public static User getInstance() {
        return instance;
    }

    private String username;
    private String password;
    private String serverip;

    public User(String username, String password, String serverip) {
        super();
        this.username = username;
        this.password = password;
        this.serverip = serverip;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setServerip(String serverip) {
        this.serverip = serverip;
    }

    public String getServerip() {
        return serverip;
    }
}
