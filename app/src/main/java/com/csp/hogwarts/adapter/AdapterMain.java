package com.csp.hogwarts.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.csp.hogwarts.HistoryActivity;
import com.csp.hogwarts.R;
import com.csp.hogwarts.databinding.ItemLoanBinding;
import com.csp.hogwarts.db.model.Loan;
import com.csp.hogwarts.utils.ImageUploader;

import java.util.ArrayList;
import java.util.List;

public class AdapterMain extends RecyclerView.Adapter<AdapterMain.ViewHolder> {

    public List<Loan> lstLoans = new ArrayList<>();
    private Context context;

    public AdapterMain(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterMain.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLoanBinding binding = ItemLoanBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMain.ViewHolder holder, int position) {
        Loan loan = lstLoans.get(position);
        bind(holder.binding, loan);
        holder.binding.getRoot().setOnClickListener(v -> {
            Intent screen = new Intent(context, HistoryActivity.class);
            screen.putExtra("loan",loan);
            context.startActivity(screen);
        });
    }

    @Override
    public int getItemCount() {
        return lstLoans.size();
    }

    public static void bind(ItemLoanBinding binding, Loan loan){
        binding.txtName.setText(loan.name);
        binding.txtMobile.setText(loan.mobile);
        binding.txtAmount.setText(String.valueOf(Math.abs(loan.balance)));
        Glide.with(binding.profile.getContext()).load(ImageUploader.getProfileUrl(loan.mobile)).into(binding.profile);
        if(loan.balance<0){
            binding.txtAmount.setTextColor(binding.txtAmount.getContext().getColor(R.color.red));
        }else{
            binding.txtAmount.setTextColor(binding.txtAmount.getContext().getColor(R.color.green));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemLoanBinding binding;
        public ViewHolder(@NonNull ItemLoanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
