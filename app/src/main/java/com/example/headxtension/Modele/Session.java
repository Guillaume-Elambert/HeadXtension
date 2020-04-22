package com.example.headxtension.Modele;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session{
    private static Session mInstance;
    private SharedPreferences mMyPreferences;

    private Session(){ }

    public static Session getInstance(){
        if (mInstance == null) mInstance = new Session();
        return mInstance;
    }

    public void Initialize(Context ctxt){
        mMyPreferences = PreferenceManager.getDefaultSharedPreferences(ctxt);
    }

    public void setStringPreference(String key, String value){
        mMyPreferences.edit().putString(key, value).apply();
    }

    public String getStringPreference(String key){
        return mMyPreferences.getString(key, "");
    }

    public void setAuthenticationState(boolean isLogin){
        mMyPreferences.edit().putBoolean("isLogin",isLogin).apply();
    }

    public void setRegistrationState(boolean isRegister){
        mMyPreferences.edit().putBoolean("isRegister",isRegister).apply();
    }

    public boolean getAuthenticationState(){
        return mMyPreferences.getBoolean("isLogin",false);
    }

    public boolean getRegistrationState(){
        return mMyPreferences.getBoolean("isRegister",false);
    }


}
