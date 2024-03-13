package com.csp.hogwarts.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;


import java.util.Objects;

public class LoadingDialog extends Dialog {
    public LoadingDialog(@NonNull Context context) {
        super(context);
        super.setCancelable(false);
        ProgressBar progressBar = new ProgressBar(context);
        Objects.requireNonNull(super.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        super.setContentView(progressBar);
    }
}
