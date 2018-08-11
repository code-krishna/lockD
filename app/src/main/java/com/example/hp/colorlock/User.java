package com.example.hp.colorlock;

public class User {
    private String email;
    private String password;
    private String user_name;
    private String uid;

    public User( String email, String pass, String name,String uid) {
        this.email = email;
        this.password = pass;
        this.user_name = name;
        this.uid = uid;
    }
    public User(String uid,String name){
        this.user_name=name;
        this.uid=uid;
    }
    public User(){

    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return user_name;
    }

    public void setName(String name) {
        this.user_name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
