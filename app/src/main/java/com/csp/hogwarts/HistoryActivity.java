package com.csp.hogwarts;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import com.csp.hogwarts.adapter.AdapterHistory;
import com.csp.hogwarts.adapter.AdapterMain;
import com.csp.hogwarts.databinding.ActivityHistoryBinding;
import com.csp.hogwarts.db.model.Loan;
import com.csp.hogwarts.dialogs.TransactionDialog;

public class HistoryActivity extends AppCompatActivity {
    private ActivityHistoryBinding binding;
    private TransactionDialog transactionDialog;
    private AdapterHistory adapterHistory;
    private Loan loan;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        loan = getIntent().getSerializableExtra("loan", Loan.class);
        adapterHistory = new AdapterHistory();
        transactionDialog = new TransactionDialog(HistoryActivity.this);

        binding.histRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.histRecyclerView.setAdapter(adapterHistory);

        binding.btnGave.setOnClickListener(v -> {
            transactionDialog.show(true, loan);
        });
        binding.btnGot.setOnClickListener(v->{
            transactionDialog.show(false, loan);
        });

        loadTransactions();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    private void loadTransactions() {
        MyApp.db.transaction().getAll(loan.loanId).observe(this, transactions -> {
            if(transactions!=null){
                adapterHistory.lstTransactions = transactions;
                adapterHistory.notifyDataSetChanged();
                binding.histRecyclerView.scrollToPosition(transactions.size()-1);
            }
        });
        MyApp.db.loan().observeLoan(loan.loanId).observe(this, loan -> AdapterMain.bind(binding.itemLoan, loan));
    }
}