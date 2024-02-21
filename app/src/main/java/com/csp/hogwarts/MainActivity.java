package com.csp.hogwarts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.csp.hogwarts.auth.Auth;
import com.csp.hogwarts.auth.AuthActivity;
import com.csp.hogwarts.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        auth = new Auth(this);
    }

    @Override
    protected void onResume() {
        if(!auth.isAuthenticated())
            startActivity(new Intent(this, AuthActivity.class));
        super.onResume();
    }
}