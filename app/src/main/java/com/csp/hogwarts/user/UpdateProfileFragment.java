package com.csp.hogwarts.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.preprocess.BitmapDecoder;
import com.cloudinary.android.preprocess.BitmapEncoder;
import com.cloudinary.android.preprocess.DimensionsValidator;
import com.cloudinary.android.preprocess.ImagePreprocessChain;
import com.cloudinary.android.preprocess.Limit;
import com.csp.hogwarts.MainActivity;
import com.csp.hogwarts.MyApp;
import com.csp.hogwarts.R;
import com.csp.hogwarts.auth.AuthActivity;
import com.csp.hogwarts.auth.UpdatePasswordFragment;
import com.csp.hogwarts.auth.User;
import com.csp.hogwarts.databinding.FragmentUpdateProfileBinding;
import com.csp.hogwarts.dialogs.LoadingDialog;
import com.csp.hogwarts.net.WebClient;
import com.csp.hogwarts.utils.ImagePicker;
import com.csp.hogwarts.utils.ImageUploader;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileFragment extends Fragment implements WebClient.OnResult {
    private static final String TAG = "UpdateProfileFragment";
    private FragmentUpdateProfileBinding binding;
    private LoadingDialog loadingDialog;
    private Uri profileUri=null;
    private HashMap<String , String > profile;
    private String name;
    public UpdateProfileFragment() {}
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false);
        loadingDialog = new LoadingDialog(requireActivity());

        binding.editName.setText(User.name);
        Glide.with(requireActivity()).load(ImageUploader.getProfileUrl(User.mobile)).into(binding.imgProfile);
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
                    requireActivity().startActivity(new Intent(requireActivity(), MainActivity.class));
                    requireActivity().finish();
                }
            }
        });
        ImageUploader.init(requireActivity());
        binding.imgProfile.setOnClickListener(v -> {
            ((UserActivity)requireActivity()).imagePicker.pick(uri -> {
                profileUri = uri;
                Glide.with(requireActivity()).load(uri).into(binding.imgProfile);
            });
        });
        binding.btnSave.setOnClickListener(this::saveProfileImage);
        return binding.getRoot();
    }

    public void saveProfileImage(View v){
        name = binding.editName.getText().toString();
        if(name.length()==0){
            Toast.makeText(requireActivity(), "Name is required", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingDialog.show();
        if(profileUri!=null){
            MediaManager.get().upload(profileUri)
                    .option("folder","/hogwarts/profiles/")
                    .option("public_id", User.mobile)
                    .preprocess(new ImagePreprocessChain()
                            .loadWith(new BitmapDecoder(1024,1024))
                            .addStep(new Limit(1024, 1024))
                            .addStep(new DimensionsValidator(10,10,1024,1024))
                            .saveWith(new BitmapEncoder(BitmapEncoder.Format.PNG, 80))
                    )
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            Log.d(TAG, "onStart: uploading image");
                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {

                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            Log.d(TAG, "onSuccess: image uploaded...");
                            updateProfile();
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            Log.e(TAG, "onError: "+error );
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            Log.d(TAG, "onReschedule: recheduled...");
                        }
                    })
                    .dispatch(requireActivity());
        }else{
            updateProfile();
        }
    }

    private void updateProfile() {
        HashMap<String ,String > req = new HashMap<>();
        req.put("name",name);
        WebClient.doPost("/user/update-profile", MyApp.gson.toJson(req), this);
    }

    @Override
    public void onSuccess(@NonNull String response) {
        loadingDialog.dismiss();
        User.updateProfile(name);
        if(getArguments()!=null && getArguments().getBoolean("showProfile")){
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in,R.anim.slide_out);
            ft.replace(R.id.fragmentContainer, new ShowProfileFragment());
            loadingDialog.dismiss();
            ft.commit();
        }else{
            Log.d(TAG, "onSuccess: launching MA");
            requireActivity().startActivity(new Intent(requireActivity(), MainActivity.class));
            requireActivity().finish();
        }
    }

    @Override
    public void onFailure(@NonNull JSONObject error) {
        try {
            String message = error.getString("message");
            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Log.d(TAG, "onFailure: e");
        }finally {
            loadingDialog.dismiss();
        }
    }
}