package com.example.hp.colorlock;

public class User {
    private String user_id;
    private String email;

    public User(String uid,String email){
        user_id=uid;
        this.email=email;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }
}
