package com.csp.hogwarts.auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.csp.hogwarts.R;
import com.csp.hogwarts.databinding.FragmentOtpBinding;
import com.csp.hogwarts.net.NetClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

public class OtpFragment extends Fragment {
    private static final String TAG = "OtpFragment";
  private FragmentOtpBinding binding;
  private Auth auth;
  private NetClient netClient;
    public OtpFragment() {}
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOtpBinding.inflate(inflater);
        netClient = new NetClient(getActivity());
        auth = new Auth(getActivity());

        binding.btnVerify.setOnClickListener(v -> {
            String otp = Objects.requireNonNull(binding.editTxtOtp.getEditText()).getText().toString();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("verificationId", requireArguments().getString("verificationId"));
                jsonObject.put("otp",otp);
                netClient.doPost("/auth/verify-otp", jsonObject.toString(), this::handleResponse);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        Objects.requireNonNull(binding.editTxtOtp.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.editTxtOtp.setErrorEnabled(false);
            }
        });
        return binding.getRoot();
    }

    private void handleResponse(String data){
        try {
            JSONObject result = new JSONObject(data);
            if(!result.isNull("error")){
                Log.d(TAG, "handleResponse: "+data);
                JSONObject error = result.getJSONObject("error");
                binding.editTxtOtp.setError(error.getString("message"));
                return;
            }
            auth.setAccessToken(result.getString("accessToken"));
            requireActivity().finish();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}