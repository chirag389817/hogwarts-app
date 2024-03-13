package com.csp.hogwarts.adapter;

import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.csp.hogwarts.R;
import com.csp.hogwarts.databinding.ItemBatchBinding;
import com.csp.hogwarts.databinding.ItemHistoryBinding;
import com.csp.hogwarts.db.model.Transaction;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.CornerTreatment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {
    private LocalDateTime prev = null;
    public List<Transaction> lstTransactions = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryBinding binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull AdapterHistory.ViewHolder holder, int position) {
        Transaction transaction = lstTransactions.get(position);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,10,0,10);
        holder.binding.cardView.setRadius(40);
        if(transaction.isSent){
            holder.binding.getRoot().setGravity(Gravity.END);
            params.setMarginStart(200);
            holder.binding.cardView.setShapeAppearanceModel(holder.binding.cardView.getShapeAppearanceModel()
                    .toBuilder()
                    .setBottomRightCorner(CornerFamily.ROUNDED,0)
                    .build());
        }else{
            holder.binding.getRoot().setGravity(Gravity.START);
            params.setMarginEnd(200);
            holder.binding.cardView.setShapeAppearanceModel(holder.binding.cardView.getShapeAppearanceModel()
                    .toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED,0)
                    .build());
        }
        holder.binding.cardView.setLayoutParams(params);
        if(transaction.amount<0){
            holder.binding.txtAmount.setTextColor(holder.binding.txtAmount.getContext().getColor(R.color.red));
        }else{
            holder.binding.txtAmount.setTextColor(holder.binding.txtAmount.getContext().getColor(R.color.green));
        }

        holder.binding.txtAmount.setText(String.valueOf(Math.abs(transaction.amount)));
        holder.binding.txtNote.setText(transaction.note);
        holder.binding.txtTime.setText(transaction.postedOn.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+" "+transaction.postedOn.truncatedTo(ChronoUnit.MINUTES).toLocalTime());

    }

    @Override
    public int getItemCount() {
        return lstTransactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHistoryBinding binding;
        public ViewHolder(@NonNull ItemHistoryBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
