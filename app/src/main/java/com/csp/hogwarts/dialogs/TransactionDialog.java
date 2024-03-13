package com.csp.hogwarts.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.csp.hogwarts.MyApp;
import com.csp.hogwarts.R;
import com.csp.hogwarts.databinding.DialogTransactionBinding;
import com.csp.hogwarts.db.model.Loan;
import com.csp.hogwarts.db.model.Transaction;
import com.csp.hogwarts.net.WebClient;
import com.csp.hogwarts.net.requests.TransactionReq;
import com.csp.hogwarts.net.responses.TransactionRes;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Objects;

public class TransactionDialog extends Dialog implements WebClient.OnResult {
    private static final String TAG = "TransactionDialog";

    private boolean isDlgGave;
    private final DialogTransactionBinding binding;
    private final LoadingDialog loadingDialog;
    private final DateTimePicker dateTimePicker;
    private final Context context;
    private Transaction transaction;

    private String loanId;
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public TransactionDialog(Context context){
        super(context,R.style.Theme_Hogwarts_Dialog );
        this.context = context;
        binding = DialogTransactionBinding.inflate(((AppCompatActivity)context).getLayoutInflater());

        loadingDialog = new LoadingDialog(context);

        dateTimePicker = new DateTimePicker(dateTime -> {
            binding.btnDateTime.setText(dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        });
        binding.btnDateTime.setOnClickListener(v -> {
            dateTimePicker.show(((AppCompatActivity) context).getSupportFragmentManager(), "Date Time");
        });

        binding.btnAdd.setOnClickListener(v->{
            try {
                double amount = Double.parseDouble(binding.editAmount.getText());
                String note = binding.editNote.getText();
                if(amount<=0){
                    binding.editAmount.setError("Invalid amount");
                    return;
                }
                loadingDialog.show();
                double amountReal= isDlgGave ? -amount : amount;
                transaction = new Transaction(loanId,amountReal,note,dateTimePicker.getDateTime(), LocalDateTime.now(),true);
                TransactionReq transactionReq = new TransactionReq(transaction.loanId,transaction.amount,transaction.note, transaction.dateTime, transaction.postedOn);
                WebClient.doPost(TransactionReq.URL,MyApp.gson.toJson(transactionReq),this);
            }catch (NumberFormatException e){
                binding.editAmount.setError("Invalid amount");
                e.printStackTrace();
            }

        });
        super.setCanceledOnTouchOutside(false);
        super.setContentView(binding.getRoot());
    }

    private void reset(){
        binding.editAmount.setText("");
        binding.editNote.setText("");
        binding.btnDateTime.setText("add Date");
        dateTimePicker.reset();
    }

    public void show(boolean isDlgGave, Loan loan){
        this.isDlgGave = isDlgGave;
        this.loanId = loan.loanId;
        reset();
        if(isDlgGave){
            binding.txtTitle.setText("You Gave to "+loan.name);
        }else{
            binding.txtTitle.setText("You Got from "+loan.name);
        }
        super.show();
        binding.editAmount.requestFocus();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSuccess(@NonNull String response) {
        TransactionRes transactionRes = MyApp.gson.fromJson(response, TransactionRes.class);
        transaction.transactionId = transactionRes.transactionId;
        new Thread(() -> {
            MyApp.db.transaction().insert(transaction);
            MyApp.db.loan().updateBalance(transaction.loanId, transaction.amount, LocalDateTime.now());
            ((AppCompatActivity)context).runOnUiThread(() -> {
                loadingDialog.dismiss();
                TransactionDialog.super.dismiss();
            });
        }).start();
    }

    @Override
    public void onFailure(@NonNull JSONObject error) {
        Log.d(TAG, "onFailure: "+error);
        String message = null;
        try {
            message = error.getString("message");
            switch (error.getString("field")){
                case "amount":{
                    binding.editAmount.setError(message);
                    break;
                }
                case "note":{
                    binding.editNote.setError(message);
                    break;
                }
                default:{
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            }
        }catch (JSONException e) {
            if(message!=null && message.length()>0)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onFailure: error here...", e);
        } finally {
            loadingDialog.dismiss();
        }
    }
}
