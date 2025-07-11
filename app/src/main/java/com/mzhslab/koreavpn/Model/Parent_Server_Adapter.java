package com.mzhslab.koreavpn.Model;

import java.util.ArrayList;

public class Parent_Server_Adapter {

    private String Country_name, ip_address, city, ip_ping, config, username, password;
    public int server_id, ping_val, type;
    public ArrayList<Cities_Server_List_Pojo> citiesList;

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setCountry_name(String Country_name) {
        this.Country_name = Country_name;
    }

    public String getCountry_name() {
        return Country_name;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setPing_val(int ping_val) {
        this.ping_val = ping_val;
    }

    public int getPing_val() {
        return ping_val;
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

    public void setIp_ping(String ip_ping) {
        this.ip_ping = ip_ping;
    }

    public String getIp_ping() {
        return ip_ping;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getConfig() {
        return config;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}





//package com.mzhslab.koreavpn.homeui.main;
//
//import java.util.ArrayList;
//
//
//public class Parent_Server_Adapter {
//
//    private int server_id, type,ping_val , capacity;
//    private String Country_name, Flag, ip, username, password,city,ping_ip, host_name;
//    public ArrayList<Cities_Server_List_Pojo> citiesList;
//
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//    public String getCity() {
//        return city;
//    }
//
//    public void setHost_name(String host_name) {
//        this.host_name = host_name;
//    }
//    public String getHost_name() {
//        return host_name;
//    }
//
//    public void setPing_ip(String ping_ip) {
//        this.ping_ip = ping_ip;
//    }
//    public String getPing_ip() {
//        return ping_ip;
//    }
//
//    public void setPing_val(int ping_val) {
//        this.ping_val = ping_val;
//    }
//    public int getPing_val() {
//        return ping_val;
//    }
//
//    public void setServer_id(int server_id) {
//        this.server_id = server_id;
//    }
//
//    public int getServer_id() {
//        return server_id;
//    }
//
//    public void setCountry_name(String Country_name) {
//
//        this.Country_name = Country_name;
//    }
//
//    public String getCountry_name() {
//
//        return Country_name;
//    }
//
//
//    public void setCapacity(int capacity) {
//        this.capacity = capacity;
//    }
//    public int getCapacity() {
//
//        return capacity;
//    }
//
//
//    public void setUsername(String username) {
//
//        this.username = username;
//    }
//
//    public String getUsername() {
//
//        return username;
//    }
//
//
//    public void setPassword(String password) {
//
//        this.password = password;
//    }
//
//    public String getPassword() {
//
//        return password;
//    }
//
//
//    public void setFlag(String Flag) {
//        this.Flag = Flag;
//    }
//
//    public String getFlag() {
//        return Flag;
//    }
//
//    public void setIp(String ip) {
//        this.ip = ip;
//    }
//
//    public String getIp() {
//        return ip;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public int getType() {
//        return type;
//    }
//}
