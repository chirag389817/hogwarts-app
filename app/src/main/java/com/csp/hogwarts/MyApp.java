package com.csp.hogwarts;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Room;

import com.csp.hogwarts.auth.Auth;
import com.csp.hogwarts.auth.User;
import com.csp.hogwarts.db.DataBase;
import com.csp.hogwarts.net.WebClient;
import com.csp.hogwarts.utils.Notifications;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyApp extends Application {
    public static DataBase db;
    public static Gson gson;
    JsonDeserializer<LocalDateTime> deserializer = (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_DATE_TIME);
        }
        return null;
    };
    JsonSerializer<LocalDateTime> serializer = (src, typeOfSrc, context) -> {
        String dateTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTime = src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        assert dateTime != null;
        return new JsonPrimitive(dateTime);
    };
    public static Auth auth;

    @Override
    public void onCreate() {
        super.onCreate();
        db = Room.databaseBuilder(getApplicationContext(), DataBase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
        auth = new Auth(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, deserializer)
                    .registerTypeAdapter(LocalDateTime.class, serializer)
                    .create();
        }
        WebClient.setAccessToken(auth.getAccessToken());
        WebClient.context=getApplicationContext();
        Notifications.createNotificationChannel(getApplicationContext());
        User.init(getApplicationContext());
    }

}
