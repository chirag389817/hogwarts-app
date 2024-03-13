package com.csp.hogwarts.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.csp.hogwarts.R;
import com.csp.hogwarts.databinding.ActivityUserBinding;
import com.csp.hogwarts.utils.ImagePicker;

public class UserActivity extends AppCompatActivity {
    private ActivityUserBinding binding;
    public ImagePicker imagePicker = new ImagePicker(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        boolean updateProfile = getIntent().getBooleanExtra("updateProfile",false);
        if(updateProfile){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);
            UpdateProfileFragment profileFragment = new UpdateProfileFragment();
            ft.replace(R.id.fragmentContainer, profileFragment);
            ft.commit();
        }
    }
}