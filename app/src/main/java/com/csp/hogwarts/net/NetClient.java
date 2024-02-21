package com.csp.hogwarts.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.csp.hogwarts.auth.Auth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetClient {
    public static final String SERVER_BASE_URL = "https://hogwarts-server-five.vercel.app";
    private static final String TAG = "NetClient";
    private final Context context;
    private final String accessToken;

    public NetClient(Context context){
        this.context = context;
        accessToken = new Auth(context).getAccessToken();
    }

    public void doGet(String url, Map<String,String> params, OnComplete onComplete){
        new Thread(() -> {
            HttpUrl.Builder httpUrlBuilder = Objects.requireNonNull(HttpUrl.parse(SERVER_BASE_URL+url)).newBuilder();
            if(params!=null)
                for(Map.Entry<String, String > param : params.entrySet())
                    httpUrlBuilder.addQueryParameter(param.getKey(), param.getValue());
            OkHttpClient client = new OkHttpClient();
            Log.d(TAG, "doGet: accessToken "+accessToken);
            Request request = new Request.Builder()
                    .header("access-token",accessToken)
                    .url(httpUrlBuilder.build())
                    .get()
                    .build();
            try {
                Response response = client.newCall(request).execute();
                final String data = Objects.requireNonNull(response.body()).string();
                ((AppCompatActivity)context).runOnUiThread(() -> onComplete.onComplete(data));
            } catch (IOException e) {
                Log.d(TAG, "doGet: error...");
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void doPost(String url, String strBody, OnComplete onComplete){
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(strBody, MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .header("access-token",accessToken)
                    .url(SERVER_BASE_URL+url).post(body).build();
            try {
                Response response = client.newCall(request).execute();
                final String data = Objects.requireNonNull(response.body()).string();
                ((AppCompatActivity)context).runOnUiThread(() -> onComplete.onComplete(data));
            } catch (IOException e) {
                Log.d(TAG, "doPost: error...");
                throw new RuntimeException(e);
            }
            
        }).start();
    }

    public interface OnComplete{
        void onComplete(String data);
    }
}
