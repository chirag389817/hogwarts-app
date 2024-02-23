package com.csp.hogwarts.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csp.hogwarts.databinding.ItemHistoryContainerBinding;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {
    @NonNull
    @Override
    public AdapterHistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHistoryContainerBinding binding = ItemHistoryContainerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHistory.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 25;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHistoryContainerBinding binding;
        public ViewHolder(@NonNull ItemHistoryContainerBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
