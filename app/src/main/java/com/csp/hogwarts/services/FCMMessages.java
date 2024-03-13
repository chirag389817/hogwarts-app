package com.csp.hogwarts.services;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Room;

import com.csp.hogwarts.MyApp;
import com.csp.hogwarts.db.DataBase;
import com.csp.hogwarts.db.model.Loan;
import com.csp.hogwarts.db.model.Transaction;
import com.csp.hogwarts.net.WebClient;
import com.csp.hogwarts.net.requests.FCMUpdateReq;
import com.csp.hogwarts.utils.LoanFetcher;
import com.csp.hogwarts.utils.Notifications;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Reader;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class FCMMessages extends FirebaseMessagingService {
    private static final String TAG = "FCMMessages";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "onNewToken: "+token);
        MyApp.auth.setFCMToken(token);
        MyApp.auth.syncFCMToken();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        new Thread(() -> {
            Log.d(TAG, "onMessageReceived: starting ");
            if(message.getData().size() == 0) return;
            Transaction transaction = MyApp.gson.fromJson(message.getData().get("transaction"), Transaction.class);
            Loan loan = MyApp.db.loan().getLoan(transaction.loanId);
            if(loan==null){
                Log.d(TAG, "onMessageReceived: getting loan");
                LoanFetcher.fetch(getApplicationContext(),transaction.loanId);
            }else{
                Log.d(TAG, "onMessageReceived: adding tra");
                MyApp.db.transaction().insert(transaction);
                MyApp.db.loan().updateBalance(transaction.loanId, transaction.amount, LocalDateTime.now());
                Notifications.show(getApplicationContext(),transaction);
            }
        }).start();
    }
}
