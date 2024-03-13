package com.csp.hogwarts.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csp.hogwarts.MyApp;
import com.csp.hogwarts.R;
import com.csp.hogwarts.databinding.FragmentUpdatePasswordBinding;
import com.csp.hogwarts.dialogs.LoadingDialog;
import com.csp.hogwarts.net.WebClient;
import com.csp.hogwarts.user.ShowProfileFragment;
import com.csp.hogwarts.user.UpdateProfileFragment;
import com.csp.hogwarts.user.UserActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdatePasswordFragment extends Fragment implements View.OnClickListener, WebClient.OnResult {

    private FragmentUpdatePasswordBinding binding;
    private LoadingDialog loadingDialog;
    public UpdatePasswordFragment() {}
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdatePasswordBinding.inflate(inflater, container, false);
        loadingDialog = new LoadingDialog(requireActivity());
        binding.btnUpdate.setOnClickListener(this);
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(getArguments()!=null && getArguments().getBoolean("showProfile")){
                    FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);
                    ft.replace(R.id.fragmentContainer, new ShowProfileFragment());
                    loadingDialog.dismiss();
                    ft.commit();
                }else{
                    Intent intent = new Intent(requireActivity(), UserActivity.class);
                    intent.putExtra("updateProfile",true);
                    requireActivity().startActivity(intent);
                    requireActivity().finish();
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        String password = binding.editTxtPass.getText();
        String passwordConfirm = binding.editTxtPassConfirm.getText();
        if(password.length()<6 || password.length()>20){
            binding.editTxtPass.setError("Password should be 6 to 20 digit long");
            return;
        }
        if(!password.equals(passwordConfirm)){
            binding.editTxtPassConfirm.setError("Both passwords should be same");
            return;
        }
        loadingDialog.show();
        Map<String, Object> req = new HashMap<>();
        req.put("password", password);
        WebClient.doPost("/user/update-password", MyApp.gson.toJson(req), this);
    }

    @Override
    public void onSuccess(@NonNull String response) {
        loadingDialog.dismiss();
        if(getArguments()!=null && getArguments().getBoolean("showProfile")){
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);
            ft.replace(R.id.fragmentContainer, new ShowProfileFragment());
            loadingDialog.dismiss();
            ft.commit();
        }else{
            Intent intent = new Intent(requireActivity(), UserActivity.class);
            intent.putExtra("updateProfile",true);
            requireActivity().startActivity(intent);
            requireActivity().finish();
        }
    }

    @Override
    public void onFailure(@NonNull JSONObject error) {
        try {
            binding.editTxtPass.setError(error.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            loadingDialog.dismiss();
        }
    }
}