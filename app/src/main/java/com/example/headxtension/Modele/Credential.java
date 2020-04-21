package com.example.headxtension.Modele;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Credential {

    private int id;
    private String name, url, username, password;

    public Credential(int id, String name, @NonNull String url, @NonNull String username, @NonNull String password){
        this.id = id;
        this.name = name;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
