package com.csp.hogwarts.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.csp.hogwarts.MyApp;
import com.csp.hogwarts.net.WebClient;
import com.csp.hogwarts.net.requests.FCMUpdateReq;

public class Auth {

    private final SharedPreferences sp;
    private final Context context;

    public Auth(Context context){
        this.context = context;
        sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
    }
    public String  getAccessToken(){
        return sp.getString("accessToken","");
    }

    public void setAccessToken(String accessToken){
        sp.edit().putString("accessToken",accessToken).apply();
        WebClient.setAccessToken(accessToken);
    }

    public boolean checkAuthentication(){
        boolean isAuthenticated =  sp.getString("accessToken",null) != null;
        if(!isAuthenticated){
            Intent intent = new Intent(context, AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        return isAuthenticated;
    }

    public void signOut(){
        sp.edit().remove("accessToken").apply();
        Intent intent = new Intent(context, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public void setFCMToken(@NonNull String fcmToken){
        sp.edit().putString("fcmToken", fcmToken).apply();
    }

    public void syncFCMToken(){
        new Thread(() -> {
            String fcmToken = sp.getString("fcmToken", "");
            FCMUpdateReq fcmReq = new FCMUpdateReq(fcmToken);
            WebClient.doPost(FCMUpdateReq.URL, MyApp.gson.toJson(fcmReq));
        }).start();
    }
}
