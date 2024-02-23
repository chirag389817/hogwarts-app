package com.csp.hogwarts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.csp.hogwarts.adapter.AdapterHistory;
import com.csp.hogwarts.databinding.ActivityHistoryBinding;
import com.csp.hogwarts.databinding.DialogTransactionBinding;

import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {
    private ActivityHistoryBinding binding;
    private Dialog dialog;
    private boolean isDlgGave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        binding.histRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.histRecyclerView.setAdapter(new AdapterHistory());

        DialogTransactionBinding dialogBinding = DialogTransactionBinding.inflate(getLayoutInflater());
        dialogBinding.editAmount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dialogBinding.editAmount.setErrorEnabled(false);
            }
        });
        dialogBinding.btnAdd.setOnClickListener(v->{
            try {
                double amount = Double.parseDouble(dialogBinding.editAmount.getEditText().getText().toString());
                String note = Objects.requireNonNull(dialogBinding.editNote.getText()).toString();
                if(amount<=0){
                    dialogBinding.editAmount.setError("Invalid amount");
                    return;
                }
                addTransaction(amount, note);
            }catch (NumberFormatException e){
                dialogBinding.editAmount.setError("Invalid amount");
                e.printStackTrace();
            }

        });

        dialog = new Dialog(this, R.style.Theme_Hogwarts_Dialog);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setCancelable(true);

        binding.btnGave.setOnClickListener(v -> {
            dialogBinding.txtTitle.setText("You Gave");
            isDlgGave=true;
            dialog.show();
        });
        binding.btnGot.setOnClickListener(v->{
            dialogBinding.txtTitle.setText("You Got");
            isDlgGave=false;
            dialog.show();
        });
    }

    private void addTransaction(double amount, String note) {

    }
}