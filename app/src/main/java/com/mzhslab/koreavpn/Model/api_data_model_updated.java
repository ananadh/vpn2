package com.mzhslab.koreavpn.Model;

public class api_data_model_updated {

    private String Country_name, city,ip_ping,config,username,password;
    public int server_id,capacity,ping_val,type;

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

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public int getCapacity() {
        return capacity;
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
