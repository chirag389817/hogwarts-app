package com.csp.hogwarts.auth;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.csp.hogwarts.MyApp;
import com.csp.hogwarts.R;
import com.csp.hogwarts.databinding.FragmentMobileBinding;
import com.csp.hogwarts.net.WebClient;
import com.csp.hogwarts.dialogs.LoadingDialog;
import com.csp.hogwarts.net.requests.VerifyMobileReq;
import com.csp.hogwarts.net.responses.VerifyMobileRes;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MobileFragment extends Fragment implements WebClient.OnResult {
    private static final String TAG = "MobileFragment";
    private FragmentMobileBinding binding;
    private LoadingDialog loadingDialog;
    public MobileFragment() {}
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMobileBinding.inflate(inflater, container, false);
        loadingDialog = new LoadingDialog(requireActivity());

        binding.btnContinue.setOnClickListener(v -> {
            String mobile = binding.editTxtMobile.getText();
            VerifyMobileReq mobileReq = new VerifyMobileReq(mobile);
            if(!mobileReq.isValid())
                binding.editTxtMobile.setError("Invalid mobile number");
            else
                verifyMobile(mobileReq);
        });

        // handle on back press
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finishAffinity();
            }
        });

        return binding.getRoot();
    }

    private void verifyMobile(VerifyMobileReq mobileReq) {
        loadingDialog.show();
        WebClient.doPost(VerifyMobileReq.URL, MyApp.gson.toJson(mobileReq),this);
    }

    @Override
    public void onSuccess(@NonNull String response) {
        VerifyMobileRes mobileResponse = MyApp.gson.fromJson(response, VerifyMobileRes.class);
        Bundle bundle = new Bundle();
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);
        if(mobileResponse.otpSent){
            bundle.putString("verificationId", mobileResponse.verificationId);
            OtpFragment otpFragment = new OtpFragment();
            otpFragment.setArguments(bundle);
            ft.replace(R.id.fragmentContainer, otpFragment);
        }else{
            bundle.putString("userId", mobileResponse.userId);
            PasswordFragment passwordFragment = new PasswordFragment();
            passwordFragment.setArguments(bundle);
            ft.replace(R.id.fragmentContainer, passwordFragment);
        }
        loadingDialog.dismiss();
        ft.commit();
    }

    @Override
    public void onFailure(@NonNull JSONObject error) {
        try {
            binding.editTxtMobile.setError(error.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            loadingDialog.dismiss();
        }
    }
}