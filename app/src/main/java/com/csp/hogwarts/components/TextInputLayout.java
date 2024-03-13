package com.csp.hogwarts.components;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class TextInputLayout extends com.google.android.material.textfield.TextInputLayout {
    public TextInputLayout(@NonNull Context context) {
        super(context);
    }
    public TextInputLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }
    public TextInputLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addOnEditTextAttachedListener(@NonNull OnEditTextAttachedListener listener) {
        super.addOnEditTextAttachedListener(textInputLayout -> Objects.requireNonNull(textInputLayout.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputLayout.setErrorEnabled(false);
            }
        }));
    }

    public String getText(){
        return Objects.requireNonNull(super.getEditText()).getText().toString();
    }

    public void setText(@NonNull String data){
        Objects.requireNonNull(super.getEditText()).setText(data);
    }

}
