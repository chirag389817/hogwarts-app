package com.csp.hogwarts.db.model;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.csp.hogwarts.db.DateTypeConvertor;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(tableName = "loan")
public class Loan implements Serializable {
    @PrimaryKey()
    @NonNull
    public String  loanId;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String mobile;

    @ColumnInfo
    public double balance;

    @ColumnInfo
    @TypeConverters({DateTypeConvertor.class})
    public LocalDateTime dateTime;

    @Ignore
    public Loan(@NonNull String loanId, String name, String mobile) {
        this.loanId = loanId;
        this.name = name;
        this.mobile = mobile;
        this.balance = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.dateTime = LocalDateTime.now();
        }
    }

    public Loan(@NonNull String loanId, String name, String mobile, double balance, LocalDateTime dateTime) {
        this.loanId = loanId;
        this.name = name;
        this.mobile = mobile;
        this.balance = balance;
        this.dateTime = dateTime;
    }
}
