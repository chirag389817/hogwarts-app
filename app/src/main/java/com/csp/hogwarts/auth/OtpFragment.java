package com.csp.hogwarts.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csp.hogwarts.MainActivity;
import com.csp.hogwarts.MyApp;
import com.csp.hogwarts.R;
import com.csp.hogwarts.databinding.FragmentOtpBinding;
import com.csp.hogwarts.net.WebClient;
import com.csp.hogwarts.net.requests.VerifyOtpReq;
import com.csp.hogwarts.net.responses.SignInRes;
import com.csp.hogwarts.dialogs.LoadingDialog;
import com.csp.hogwarts.utils.LoanFetcher;

import org.json.JSONException;
import org.json.JSONObject;

public class OtpFragment extends Fragment implements WebClient.OnResult {
    private static final String TAG = "OtpFragment";
  private FragmentOtpBinding binding;
  private LoadingDialog loadingDialog;
  private String verificationId;
    public OtpFragment() {}
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOtpBinding.inflate(inflater);

        loadingDialog = new LoadingDialog(requireActivity());
        verificationId = requireArguments().getString("verificationId");

        binding.btnVerify.setOnClickListener(v -> {
            VerifyOtpReq verifyOtpReq = new VerifyOtpReq(verificationId, binding.editTxtOtp.getText());
            if(!verifyOtpReq.isValidOtp()) {
                binding.editTxtOtp.setError("Invalid OTP");
                return;
            }
            loadingDialog.show();
            WebClient.doPost(VerifyOtpReq.URL, MyApp.gson.toJson(verifyOtpReq), this);
        });

        return binding.getRoot();
    }

    @Override
    public void onSuccess(@NonNull String response) {
        SignInRes signInRes = MyApp.gson.fromJson(response, SignInRes.class);
        MyApp.auth.setAccessToken(signInRes.accessToken);
        LoanFetcher.fetchAll((success) -> {
            User.fetchProfile(isFetched -> {
                MyApp.auth.syncFCMToken();
                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);
                ft.replace(R.id.fragmentContainer, new UpdatePasswordFragment());
                loadingDialog.dismiss();
                ft.commit();
            });
        });
    }

    @Override
    public void onFailure(@NonNull JSONObject error) {
        try {
            binding.editTxtOtp.setError(error.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            loadingDialog.dismiss();
        }
    }
}