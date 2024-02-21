package com.csp.hogwarts.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.csp.hogwarts.databinding.ActivityAuthBinding;
import android.os.Bundle;

import com.csp.hogwarts.R;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
    }
}