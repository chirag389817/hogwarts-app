package com.csp.hogwarts.auth;

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

import com.csp.hogwarts.R;
import com.csp.hogwarts.databinding.FragmentPasswordBinding;
import com.csp.hogwarts.net.NetClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class PasswordFragment extends Fragment {
    private static final String TAG = "PasswordFragment";
    private FragmentPasswordBinding binding;
    private Auth auth;
    private NetClient netClient;
    public PasswordFragment() {}
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPasswordBinding.inflate(inflater, container, false);
        netClient = new NetClient(getActivity());
        auth = new Auth(getActivity());

        binding.btnSignIn.setOnClickListener(v -> {
            try {
                String password = Objects.requireNonNull(binding.editTxtpass.getEditText()).getText().toString();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", getArguments().get("userId"));
                jsonObject.put("password", password);
                netClient.doPost("/auth/login", jsonObject.toString(), this::handleResponse);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        binding.editTxtpass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.editTxtpass.setErrorEnabled(false);
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
                binding.editTxtpass.setError(error.getString("message"));
                return;
            }
            auth.setAccessToken(result.getString("accessToken"));
            requireActivity().finish();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}