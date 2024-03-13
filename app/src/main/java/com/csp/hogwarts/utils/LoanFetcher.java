package com.csp.hogwarts.utils;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.csp.hogwarts.MyApp;
import com.csp.hogwarts.db.model.Loan;
import com.csp.hogwarts.db.model.Transaction;
import com.csp.hogwarts.net.WebClient;
import com.csp.hogwarts.net.responses.LoanGetRes;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.List;

public class LoanFetcher {
    private static final String TAG = "LoanFetcher";
    public static void fetchAll(OnFetch onFetch){
        WebClient.doGet("/loan", new WebClient.OnResult() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(@NonNull String response) {
                new Thread(() -> {
                    List<LoanGetRes> lstLoanReGets = MyApp.gson.fromJson(response, new TypeToken<List<LoanGetRes>>(){}.getType());
                    for(LoanGetRes loanGetRes : lstLoanReGets){
                        Loan loan = new Loan(loanGetRes.loanId, loanGetRes.name, loanGetRes.mobile, loanGetRes.balance, LocalDateTime.now());
                        MyApp.db.loan().insert(loan);
                        for(Transaction transaction : loanGetRes.transactions){
                            transaction.loanId = loan.loanId;
                            MyApp.db.transaction().insert(transaction);
                        }
                    }
                    new Handler(Looper.getMainLooper()).post(() -> onFetch.onFetchFinish(true));
                }).start();
            }

            @Override
            public void onFailure(@NonNull JSONObject error) {
                onFetch.onFetchFinish(false);
            }
        });
    }

    public static void fetch(Context context, @NonNull String loanId){
        WebClient.doGet("/loan/" + loanId, new WebClient.OnResult() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(@NonNull String response) {
                new Thread(() -> {
                    LoanGetRes loanGetRes = MyApp.gson.fromJson(response, LoanGetRes.class);
                    Loan loan = new Loan(loanGetRes.loanId, loanGetRes.name, loanGetRes.mobile, loanGetRes.balance, LocalDateTime.now());
                    MyApp.db.loan().insert(loan);
                    Transaction transactionNoti = null;
                    for(Transaction transaction : loanGetRes.transactions){
                        Log.d(TAG, "onSuccess: adding "+transaction.transactionId);
                        transaction.loanId = loan.loanId;
                        MyApp.db.transaction().insert(transaction);
                        transactionNoti = transaction;
                    }
                    if(transactionNoti!=null) Notifications.show(context,transactionNoti);
                }).start();
            }

            @Override
            public void onFailure(@NonNull JSONObject error) {
                Log.d(TAG, "onFailure: "+error);
            }
        });
    }

    public interface OnFetch{
        void onFetchFinish(boolean success);
    }
}
