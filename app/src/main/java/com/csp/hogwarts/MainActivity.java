package com.csp.hogwarts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.csp.hogwarts.adapter.AdapterMain;
import com.csp.hogwarts.auth.Auth;
import com.csp.hogwarts.auth.AuthActivity;
import com.csp.hogwarts.databinding.ActivityMainBinding;
import com.csp.hogwarts.databinding.DialogLoanBinding;
import com.csp.hogwarts.net.NetClient;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NetClient netClient;
    private Dialog dialog;
    private Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        auth = new Auth(this);
        netClient = new NetClient(this);

        binding.mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.mainRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.mainRecyclerView.setAdapter(new AdapterMain());

        DialogLoanBinding loanBinding = DialogLoanBinding.inflate(getLayoutInflater());
        loanBinding.btnSave.setOnClickListener(v -> {
            String mobile = Objects.requireNonNull(loanBinding.editMobile.getText()).toString();
            if(mobile.length()!=10){
                loanBinding.editMobile.setError("Invalid mobile");
                return;
            }
            // TODO: doPost on the server
//            netClient.doPost("/loan/");
            dialog.dismiss();
        });
        dialog = new Dialog(this, R.style.Theme_Hogwarts_Dialog);
        dialog.setContentView(loanBinding.getRoot());
        dialog.setCancelable(true);

        binding.fab.setOnClickListener(v -> {
            loanBinding.editMobile.setText("");
            dialog.show();
        });
            startActivity(new Intent(this, HistoryActivity.class));
    }

    @Override
    protected void onResume() {
        if(!auth.isAuthenticated())
            startActivity(new Intent(this, AuthActivity.class));
        super.onResume();
    }
}