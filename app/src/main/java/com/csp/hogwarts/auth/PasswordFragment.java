package com.csp.hogwarts.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csp.hogwarts.MyApp;
import com.csp.hogwarts.R;
import com.csp.hogwarts.databinding.FragmentPasswordBinding;
import com.csp.hogwarts.dialogs.LoadingDialog;
import com.csp.hogwarts.net.WebClient;
import com.csp.hogwarts.net.requests.SignInReq;
import com.csp.hogwarts.net.responses.SignInRes;
import com.csp.hogwarts.user.UpdateProfileFragment;
import com.csp.hogwarts.user.UserActivity;
import com.csp.hogwarts.utils.LoanFetcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordFragment extends Fragment implements WebClient.OnResult {
    private static final String TAG = "PasswordFragment";
    private FragmentPasswordBinding binding;
    private LoadingDialog loadingDialog;
    private String userId;
    public PasswordFragment() {}
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPasswordBinding.inflate(inflater, container, false);

        loadingDialog = new LoadingDialog(requireActivity());
        userId = requireArguments().getString("userId");

        binding.btnSignIn.setOnClickListener(v -> {
            loadingDialog.show();
            SignInReq signInReq = new SignInReq(userId, binding.editTxtPass.getText());
            WebClient.doPost(SignInReq.URL, MyApp.gson.toJson(signInReq),this);
        });

        // handle on back press
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                MobileFragment mobileFragment = new MobileFragment();
                ft.replace(R.id.fragmentContainer, mobileFragment);
                loadingDialog.dismiss();
                ft.commit();
            }
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
                Intent intent = new Intent(requireActivity(), UserActivity.class);
                intent.putExtra("updateProfile",true);
                requireActivity().startActivity(intent);
                loadingDialog.dismiss();
                requireActivity().finish();
            });
        });
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