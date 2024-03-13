package com.csp.hogwarts.utils;

import android.content.Context;
import android.net.Uri;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class ImagePicker {
    private OnPickListener onPickListener;
    private final ActivityResultLauncher<PickVisualMediaRequest> resultLauncher;

    public ImagePicker(Context context) {
        resultLauncher = ((AppCompatActivity)context).registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if(uri!=null){
                        onPickListener.onPick(uri);
                    }
                }
        );
    }

    public void pick(OnPickListener onPickListener){
        this.onPickListener = onPickListener;
        resultLauncher.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
    }

    public interface OnPickListener{
        void onPick(Uri uri);
    }
}
