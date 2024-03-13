package com.csp.hogwarts.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.csp.hogwarts.db.model.Transaction;

import java.util.List;

@Dao
public interface TransactionDao {
    @Query("SELECT * FROM `transaction` WHERE loanId = :loanId ORDER BY postedOn ")
    LiveData<List<Transaction>> getAll(String loanId);

    @Insert
    void insert(Transaction transaction);
}
