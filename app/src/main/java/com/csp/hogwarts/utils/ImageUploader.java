package com.csp.hogwarts.utils;

import android.content.Context;

import com.cloudinary.android.MediaManager;
import com.csp.hogwarts.Credentials.ClodinaryCredentials;
import com.csp.hogwarts.auth.User;

import java.util.HashMap;

public class ImageUploader {
    public static final String BASE_URL = "https://res.cloudinary.com/dirqjareb/image/upload/v1709911686/hogwarts/profiles";
    private static final String TAG = "ImageUploader";
    public static void init(Context context){
        HashMap<String, String> config = new HashMap<>();
        config.put("cloud_name", ClodinaryCredentials.CLOUD_NAME);
        config.put("api_key", ClodinaryCredentials.API_KEY);
        config.put("api_secret", ClodinaryCredentials.API_SECRET);
        try {
            MediaManager.init(context,config);
        }catch (Exception ignored){}
    }

    public static String getProfileUrl(String mobile){
        return ImageUploader.BASE_URL+"/"+ mobile+".png";
    }
}
