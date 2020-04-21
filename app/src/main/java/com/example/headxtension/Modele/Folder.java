package com.example.headxtension.Modele;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Folder {

    private int id;
    private String name;
    private ArrayList<Credential> credentialArrayList;

    public Folder(int id, @NonNull String name, @Nullable ArrayList<Credential> credentialArrayList){
        this.id = id;
        this.name = name;
        this.credentialArrayList = credentialArrayList;
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

    public ArrayList<Credential> getCredentialArrayList() {
        return credentialArrayList;
    }

    public void setCredentialArrayList(ArrayList<Credential> credentialArrayList) {
        this.credentialArrayList = credentialArrayList;
    }
}
