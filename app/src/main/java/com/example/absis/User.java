package com.example.absis;

public class User {
    private int id;
    private String username, email, password,mobile,role;

    public User(int id, String username, String mobile,String role) {
        this.id = id;
        this.username = username;
        this.mobile = mobile;
        this.role=role;
    }
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getMobile() {
        return mobile;
    }
    public String getRole() {
        return role;
    }

}
