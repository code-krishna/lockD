package com.example.hp.colorlock;

import java.util.ArrayList;

public class Network {
    private String network_id;
    private String network_name;
    private String password;
    private String admin_name;
    private ArrayList<User> users=new ArrayList<>();

    public Network(String network_id, String password,String name ){
        this.network_id=network_id;
        this.network_name=name;
        this.password=password;
    }
    public Network(String network_id, String password,String name ,String admin_nam){
        this.network_id=network_id;
        this.network_name=name;
        this.password=password;
        this.admin_name=admin_nam;
    }
    public Network(String network_name,String network_id, ArrayList<User> users){
        this.network_name=network_name;
        this.network_id=network_id;
        this.users=users;
    }
    public Network(String network_name,String network_id){
        this.network_name=network_name;
        this.network_id=network_id;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public Network(){}

    public String getNetwork_id() {
        return network_id;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public void setNetwork_id(String network_id) {
        this.network_id = network_id;
    }

    public String getNetwork_name() {
        return network_name;
    }

    public void setNetwork_name(String network_name) {
        this.network_name = network_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
