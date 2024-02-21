package com.csp.hogwarts.auth;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class Auth {

    private SharedPreferences sp;

    public Auth(Context con){
        sp = con.getSharedPreferences("token", Context.MODE_PRIVATE);
    }
    public String  getAccessToken(){
        return sp.getString("accessToken","");
    }

    public void setAccessToken(String accessToken){
        sp.edit().putString("accessToken",accessToken).apply();
    }

    public boolean isAuthenticated(){
        return sp.getString("accessToken",null) != null;
    }

    public void signOut(){
        sp.edit().remove("accessToken").apply();
    }
}
