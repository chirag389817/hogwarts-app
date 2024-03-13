package com.csp.hogwarts.auth;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.csp.hogwarts.MyApp;
import com.csp.hogwarts.net.WebClient;

import org.json.JSONException;
import org.json.JSONObject;

public class User  {
    public static String name="";
    public static String mobile="";
    private static SharedPreferences sp;

    public static void init(Context context){
        sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String str = sp.getString("user","");
        try {
            JSONObject user = new JSONObject(str);
            name = user.getString("name");
            mobile = user.getString("mobile");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void updateProfile(@NonNull String name){
        try {
            JSONObject user = new JSONObject();
            user.put("name", name);
            user.put("mobile", mobile);
            sp.edit().putString("user",user.toString()).apply();
            User.name = name;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void fetchProfile(OnFetchListener onFetchListener){
        WebClient.doGet("/user/profile", new WebClient.OnResult() {
            @Override
            public void onSuccess(@NonNull String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject user = new JSONObject(jsonObject.getString("user"));
                    name = user.getString("name");
                    mobile = user.getString("mobile");
                    sp.edit().putString("user", jsonObject.getString("user")).apply();
                    if(onFetchListener!=null) onFetchListener.onFetch(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(onFetchListener!=null) onFetchListener.onFetch(false);
                }
            }

            @Override
            public void onFailure(@NonNull JSONObject error) {
                if(onFetchListener!=null) onFetchListener.onFetch(false);
            }
        });
    }

    public interface OnFetchListener{
        void onFetch(boolean isFetched);
    }
}
