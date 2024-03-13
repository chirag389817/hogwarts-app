package com.csp.hogwarts.net;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.csp.hogwarts.MyApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebClient {
    public static final String SERVER_BASE_URL = "https://hogwarts-server-five.vercel.app";
    private static final String TAG = "WebClient";
    private static final OkHttpClient okHttpClient = new OkHttpClient();
    private static String accessToken;
    public static Context context;

    public static void setAccessToken(String accessToken) {
        WebClient.accessToken = accessToken;
    }
    public static void doGet(String url, OnResult onResult){
            Request request = new Request.Builder().url(SERVER_BASE_URL+url).get().build();
            executeRequest(request, onResult);
    }

    public static void doPost(String url, String strBody) {
        RequestBody body = RequestBody.create(strBody, MediaType.get("application/json"));
        Request request = new Request.Builder().url(SERVER_BASE_URL+url).post(body).build();
        executeRequest(request);
    }

    public static void doPost(String url, String strBody, OnResult onResult){
        RequestBody body = RequestBody.create(strBody, MediaType.get("application/json"));
        Request request = new Request.Builder().url(SERVER_BASE_URL+url).post(body).build();
        executeRequest(request, onResult);
    }

    public static void executeRequest(Request request){
        new Thread(() -> {
            try {
                Request newReq = request.newBuilder().header("access-token", accessToken).build();
                Response response = okHttpClient.newCall(newReq).execute();
                String strResponse = Objects.requireNonNull(Objects.requireNonNull(response).body()).string();
                Log.d(TAG, "executeRequest: "+strResponse);
                JSONObject jsonResponse = new JSONObject(strResponse);
                JSONObject error = jsonResponse.getJSONObject("error");
                if(!error.isNull("name") && error.getString("name").equals("JsonWebTokenError")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        MyApp.auth.signOut();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }
    public static void executeRequest(Request request, OnResult onResult){
        new Thread(() -> {
            try {
                Request newReq = request.newBuilder().header("access-token", accessToken).build();
                Response response = okHttpClient.newCall(newReq).execute();
                String strResponse = Objects.requireNonNull(Objects.requireNonNull(response).body()).string();
                Log.d(TAG, "executeRequest: "+strResponse);
                if(isJSONArray(strResponse)){
                    onResult.onSuccess(strResponse);
                    return;
                }
                JSONObject jsonResponse = new JSONObject(strResponse);
                if(jsonResponse.isNull("error")){
                    new Handler(Looper.getMainLooper()).post(() -> {
                        onResult.onSuccess(strResponse);
                    });
                    return;
                }
                JSONObject error = jsonResponse.getJSONObject("error");
                if(!error.isNull("name") && error.getString("name").equals("JsonWebTokenError")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        MyApp.auth.signOut();
                    }
                    return;
                }
                new Handler(Looper.getMainLooper()).post(() -> {
                    onResult.onFailure(error);
                });
            }catch (Exception e){
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    onResult.onFailure(new JSONObject());
                });
            }
        }).start();
    }

    public static boolean isJSONArray(String strBody){
        try {
            new JSONArray(strBody);
            return true;
        } catch (JSONException ignored) {}
        return false;
    }

    public interface OnResult{
        void onSuccess(@NonNull String response);
        void onFailure(@NonNull JSONObject error);
    }
}
