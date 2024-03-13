package com.csp.hogwarts.db.model;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.csp.hogwarts.db.DateTypeConvertor;

import java.time.LocalDateTime;

@Entity(tableName = "transaction", foreignKeys = {@ForeignKey(entity = Loan.class,
    parentColumns = "loanId",childColumns = "loanId", onDelete = ForeignKey.CASCADE)})
public class Transaction {
    @NonNull
    @PrimaryKey
    public String transactionId;

    @NonNull
    @ColumnInfo
    public String loanId;

    @ColumnInfo
    public double amount;

    @ColumnInfo
    public String note;

    @ColumnInfo
    @TypeConverters({DateTypeConvertor.class})
    public LocalDateTime dateTime;

    @ColumnInfo
    @TypeConverters({DateTypeConvertor.class})
    public LocalDateTime postedOn;

    @ColumnInfo
    public boolean isSent;

    @Ignore
    public Transaction(@NonNull String transactionId, @NonNull String loanId, double amount, String note, LocalDateTime dateTime){
        this.transactionId = transactionId;
        this.loanId=loanId;
        this.amount = amount;
        this.note = note;
        this.dateTime = dateTime;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.postedOn = LocalDateTime.now();
        }
        this.isSent = true;
    }

    @Ignore
    public Transaction(@NonNull String loanId, double amount, String note, LocalDateTime dateTime, LocalDateTime postedOn, boolean isSent){
        this.loanId=loanId;
        this.amount=amount;
        this.note=note;
        this.dateTime=dateTime;
        this.postedOn=postedOn;
        this.isSent=isSent;
    }
    public Transaction(@NonNull String transactionId, double amount, String note, LocalDateTime dateTime, LocalDateTime postedOn, boolean isSent, @NonNull String loanId) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.note = note;
        this.dateTime = dateTime;
        this.postedOn = postedOn;
        this.isSent = isSent;
        this.loanId=loanId;
    }
}
