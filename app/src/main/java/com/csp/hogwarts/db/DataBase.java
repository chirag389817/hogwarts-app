package com.csp.hogwarts.db;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.csp.hogwarts.db.dao.LoanDao;
import com.csp.hogwarts.db.dao.TransactionDao;
import com.csp.hogwarts.db.model.Loan;
import com.csp.hogwarts.db.model.Transaction;

@Database(entities = {Loan.class, Transaction.class}, version = 5, exportSchema = false)
public abstract class DataBase extends RoomDatabase {
    public abstract LoanDao loan();
    public abstract TransactionDao transaction();
}
