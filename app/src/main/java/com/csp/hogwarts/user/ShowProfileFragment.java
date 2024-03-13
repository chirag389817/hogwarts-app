package com.csp.hogwarts.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.csp.hogwarts.MyApp;
import com.csp.hogwarts.R;
import com.csp.hogwarts.auth.UpdatePasswordFragment;
import com.csp.hogwarts.auth.User;
import com.csp.hogwarts.databinding.FragmentShowProfileBinding;
import com.csp.hogwarts.utils.ImagePicker;
import com.csp.hogwarts.utils.ImageUploader;

public class ShowProfileFragment extends Fragment {
    private static final String TAG = "ShowProfileFragment";
    private FragmentShowProfileBinding binding;
    public ShowProfileFragment() { }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShowProfileBinding.inflate(inflater, container, false);
        binding.txtName.setText(User.name);
        Glide.with(requireActivity()).load(ImageUploader.getProfileUrl(User.mobile)).into(binding.imgProfile);

        Bundle bundle = new Bundle();
        bundle.putBoolean("showProfile",true);

        binding.updatePass.setOnClickListener(v -> {
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);
            UpdatePasswordFragment passwordFragment = new UpdatePasswordFragment();
            passwordFragment.setArguments(bundle);
            ft.replace(R.id.fragmentContainer, passwordFragment);
            ft.commit();
        });
        binding.updateProfile.setOnClickListener(v -> {
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);
            UpdateProfileFragment profileFragment = new UpdateProfileFragment();
            profileFragment.setArguments(bundle);
            ft.replace(R.id.fragmentContainer, profileFragment);
            ft.commit();
        });
        return binding.getRoot();
    }
}