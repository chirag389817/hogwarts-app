package com.csp.hogwarts.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.csp.hogwarts.MyApp;
import com.csp.hogwarts.R;
import com.csp.hogwarts.databinding.DialogLoanBinding;
import com.csp.hogwarts.db.model.Loan;
import com.csp.hogwarts.net.WebClient;
import com.csp.hogwarts.net.requests.LoanPostReq;
import com.csp.hogwarts.net.responses.LoanPostRes;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class LoanDialog extends Dialog implements View.OnClickListener, WebClient.OnResult {
    private final DialogLoanBinding binding;
    private final LoadingDialog loadingDialog;
    private final Context context;
    public LoanDialog(@NonNull Context context) {
        super(context, R.style.Theme_Hogwarts_Dialog);
        this.context = context;
        loadingDialog = new LoadingDialog(context);
        binding = DialogLoanBinding.inflate(((AppCompatActivity)context).getLayoutInflater());
        binding.btnSave.setOnClickListener(this);

        super.setCanceledOnTouchOutside(false);
        super.setContentView(binding.getRoot());
    }

    @Override
    public void onClick(View v) {
        String mobile = binding.editMobile.getText();
        String name = binding.editName.getText();
        boolean isAnonymous = binding.chkAnonymous.isChecked();

        if (mobile.length() != 10){
            binding.editMobile.setError("Invalid mobile");
            return;
        }

        LoanPostReq loanPostReq = new LoanPostReq(name, mobile, isAnonymous);
        loadingDialog.show();
        WebClient.doPost(LoanPostReq.URL, MyApp.gson.toJson(loanPostReq),this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSuccess(@NonNull String response) {
        new Thread(() -> {
            LoanPostRes loanPostRes = MyApp.gson.fromJson(response, LoanPostRes.class);
            Loan loan = new Loan(loanPostRes.loanId, binding.editName.getText(), binding.editMobile.getText(),0, LocalDateTime.now());
            MyApp.db.loan().insert(loan);
            new Handler(Looper.getMainLooper()).post(() -> {
                loadingDialog.dismiss();
                super.dismiss();
            });
        }).start();
    }

    @Override
    public void onFailure(@NonNull JSONObject error) {
        String message = null;
        try {
            message = error.getString("message");
            switch (error.getString("field")){
                case "name": {
                    binding.editName.setError(message);
                    break;
                }
                case "mobile": {
                    binding.editMobile.setError(message);
                    break;
                }
            }
        } catch (JSONException e) {
            if(message!=null && message.length()>0)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        finally {
            loadingDialog.dismiss();
        }
    }

    public void show() {
        binding.editMobile.setText("");
        binding.editName.setText("");
        binding.chkAnonymous.setChecked(false);
        super.show();
    }
}
