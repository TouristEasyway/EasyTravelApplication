package com.example.easytravelapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytravelapplication.Model.PackageHistoryListResponse;
import com.example.easytravelapplication.R;
import com.example.easytravelapplication.databinding.ListItemPackageHistoryBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PackageHistoryAdapter extends RecyclerView.Adapter<PackageHistoryAdapter.MyViewHolder> {
    private ArrayList<PackageHistoryListResponse> responseList;
    private Context context;

    public PackageHistoryAdapter(ArrayList<PackageHistoryListResponse> responseList, Context ctx) {
        this.responseList = responseList;
        context = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemPackageHistoryBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item_package_history, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PackageHistoryListResponse response = responseList.get(position);

        holder.binding.tvUserName.setText(response.getUserName());
        holder.binding.tvPackageName.setText(response.getPackageName());
        holder.binding.tvDayNight.setText(response.getTotalDay() + "D/" + response.getTotalNight() + "N");
        holder.binding.tvStartingDate.setText(response.getStartingDate());
        holder.binding.tvEndDate.setText(response.getEndDate());

        Picasso.get().load(response.getPackageImage()).into(holder.binding.rivPackage);

    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ListItemPackageHistoryBinding binding;

        public MyViewHolder(ListItemPackageHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
