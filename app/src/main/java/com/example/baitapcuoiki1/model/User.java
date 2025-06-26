package com.example.baitapcuoiki1.model;

public class User {
    private String email;
    private int role;

    public User(String email, int role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public int getRole() {
        return role;
    }

    public String getRoleText() {
        return role == 1 ? "Quản trị viên" : "Người dùng";
    }
}
