package com.csp.hogwarts;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.csp.hogwarts.adapter.AdapterMain;
import com.csp.hogwarts.auth.AuthActivity;
import com.csp.hogwarts.databinding.ActivityMainBinding;
import com.csp.hogwarts.dialogs.LoanDialog;
import com.csp.hogwarts.user.UserActivity;
import com.csp.hogwarts.utils.Permissions;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private AdapterMain adapterMain;
    private LoanDialog loanDialog;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        // checking if the user is authenticated or not
        if(!MyApp.auth.checkAuthentication()) finish();

        new Permissions(this).askForPermissions();

        adapterMain = new AdapterMain(MainActivity.this);
        loanDialog = new LoanDialog(this);

        binding.mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.mainRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.mainRecyclerView.setAdapter(adapterMain);

        loadLoans();

        binding.fab.setOnClickListener(v -> {
            loanDialog.show();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadLoans() {
        MyApp.db.loan().getAll().observe(MainActivity.this, loans -> {
            if(loans!=null){
                adapterMain.lstLoans = loans;
                runOnUiThread(() -> adapterMain.notifyDataSetChanged());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.profile){
            startActivity(new Intent(this, UserActivity.class));
        }
        return true;
    }
}