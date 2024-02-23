package com.csp.hogwarts.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csp.hogwarts.databinding.ItemLoanBinding;

public class AdapterMain extends RecyclerView.Adapter<AdapterMain.ViewHolder> {
    @NonNull
    @Override
    public AdapterMain.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLoanBinding binding = ItemLoanBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMain.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemLoanBinding binding;
        public ViewHolder(@NonNull ItemLoanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
