package com.csp.hogwarts.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.csp.hogwarts.R;
import com.csp.hogwarts.databinding.FragmentMobileBinding;
import com.csp.hogwarts.net.NetClient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MobileFragment extends Fragment {
    private static final String TAG = "MobileFragment";
    private FragmentMobileBinding binding;
    private Gson gson = new Gson();
    private NetClient netClient;
    public MobileFragment() {}
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMobileBinding.inflate(inflater, container, false);
        netClient = new NetClient(getActivity());

        binding.btnContinue.setOnClickListener(v -> {
            String mobile = Objects.requireNonNull(binding.editTxtMobile.getEditText()).getText().toString();
            if(mobile.length()!=10){
                binding.editTxtMobile.setError("Invalid mobile number");
                return;
            }
            JSONObject json = new JSONObject();
            try {
                json.put("mobile",mobile);
                String js = json.toString();
                netClient.doPost("/auth/verify-mobile", json.toString(), this::handleResponse);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        Objects.requireNonNull(binding.editTxtMobile.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.editTxtMobile.setErrorEnabled(false);
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
                binding.editTxtMobile.setError(error.getString("message"));
                return;
            }
            Bundle bundle = new Bundle();
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);
            if(result.getBoolean("otpSent")){
                bundle.putString("verificationId", result.getString("verificationId"));
                OtpFragment otpFragment = new OtpFragment();
                otpFragment.setArguments(bundle);
                ft.replace(R.id.fragmentContainer, otpFragment);
            }else{
                bundle.putString("userId", result.getString("userId"));
                PasswordFragment passwordFragment = new PasswordFragment();
                passwordFragment.setArguments(bundle);
                ft.replace(R.id.fragmentContainer, passwordFragment);
            }
            ft.commit();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}