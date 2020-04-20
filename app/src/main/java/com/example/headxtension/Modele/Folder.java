package com.example.headxtension.Modele;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Folder {

    private int id;
    private String name;
    private ArrayList<Credentials> credentialsArrayList;

    public Folder(int id, @NonNull String name, @Nullable ArrayList<Credentials> credentialsArrayList){
        this.id = id;
        this.name = name;
        this.credentialsArrayList = credentialsArrayList;
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

    public ArrayList<Credentials> getCredentialsArrayList() {
        return credentialsArrayList;
    }

    public void setCredentialsArrayList(ArrayList<Credentials> credentialsArrayList) {
        this.credentialsArrayList = credentialsArrayList;
    }
}
