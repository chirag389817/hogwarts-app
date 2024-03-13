package com.csp.hogwarts.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.csp.hogwarts.HistoryActivity;
import com.csp.hogwarts.MyApp;
import com.csp.hogwarts.R;
import com.csp.hogwarts.db.model.Loan;
import com.csp.hogwarts.db.model.Transaction;

public class Notifications {
    private static int nId = 0;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void show(Context context, Transaction transaction){
        new Thread(() -> {
            Loan loan = MyApp.db.loan().getLoan(transaction.loanId);
            Intent intent = new Intent(context, HistoryActivity.class);
            intent.putExtra("loan", loan);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent,PendingIntent.FLAG_IMMUTABLE);
            double amount = Math.abs(transaction.amount);
            String message = transaction.amount<0 ? loan.name+" Got Rs. "+amount+" from you" :  loan.name+" GAve Rs. "+amount+" to you";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Transaction")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(loan.name)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(nId++,builder.build());
        }).start();
    }

    public static void createNotificationChannel(Context context) {
        new Thread(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Transactions";
                String description = "shows transactions";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("Transaction", name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }).start();
    }
}
