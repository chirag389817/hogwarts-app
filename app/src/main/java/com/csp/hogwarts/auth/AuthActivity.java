package com.csp.hogwarts.auth;

import androidx.appcompat.app.AppCompatActivity;

import com.csp.hogwarts.databinding.ActivityAuthBinding;
import com.csp.hogwarts.utils.ImagePicker;

import android.os.Bundle;

public class AuthActivity extends AppCompatActivity {
    private static final String TAG = "AuthActivity";
    private ActivityAuthBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
    }
}