package com.example.hp.colorlock;

public class User {
    private String EMAIL;
    private String PASS;

    public User(String email,String pass){
        this.EMAIL = email;
        this.PASS = pass;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getPASS() {
        return PASS;
    }

    public void setPASS(String PASS) {
        this.PASS = PASS;
    }
}
