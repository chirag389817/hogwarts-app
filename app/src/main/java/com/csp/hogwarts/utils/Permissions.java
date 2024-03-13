package com.csp.hogwarts.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Permissions {
    private static final String TAG = "Permissions";
    private final ActivityResultLauncher<String> requestPermissionLauncher;
    private final Context context;
    public Permissions(Context context){
        this.context = context;
        requestPermissionLauncher = ((AppCompatActivity)context).registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), isGranted -> {
                    Log.d(TAG, "Permissions: "+isGranted);
                });
    }

    public void askForPermissions(){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)== PackageManager.PERMISSION_DENIED){
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
        requestPermissionLauncher.launch("dkdkk");
    }
}
