package com.example.hp.colorlock;

import java.util.ArrayList;

public class Network {
    private String network_id;
    private ArrayList<String> users;
    private String master_user_id;
    private String network_name;

    public Network(String nid,String password,String name ){
        this.network_id=nid;
        this.network_name=name;
    }
    public Network(){}

    public void addUser_to_Network(String user_id){
        users.add(user_id);
    }
    public String getNetwork_id() {
        return network_id;
    }
    public String getMaster_user_id(){
        return master_user_id;
    }
    public void setMaster_user_id(String mid){
        this.master_user_id=mid;
    }
    public String getNetwork_name(){
        return network_name;
    }



}
