package com.csp.hogwarts.db.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import com.csp.hogwarts.db.DateTypeConvertor;
import com.csp.hogwarts.db.model.Loan;

import java.time.LocalDateTime;
import java.util.List;

import kotlinx.coroutines.flow.Flow;

@Dao
public interface LoanDao {
    @Query("SELECT * FROM loan ORDER BY dateTime DESC")
    LiveData<List<Loan>> getAll();

    @Query("SELECT * FROM loan WHERE loanId = :loanId")
    Loan getLoan(String loanId);

    @Query("SELECT * FROM loan WHERE loanId = :loanId")
    LiveData<Loan> observeLoan(String loanId);

    @Insert
    void insert(Loan loan);

    @TypeConverters({DateTypeConvertor.class})
    @Query("UPDATE loan SET balance = balance + :balance, dateTime = :dateTime  WHERE loanId = :loanId")
    void updateBalance(@NonNull String loanId, double balance, LocalDateTime dateTime);
}
