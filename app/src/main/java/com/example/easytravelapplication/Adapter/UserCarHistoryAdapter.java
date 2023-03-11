
package com.example.easytravelapplication.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytravelapplication.Model.CarHistoryResponse;
import com.example.easytravelapplication.R;
import com.example.easytravelapplication.databinding.ItemUserDashboardBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class UserCarHistoryAdapter extends RecyclerView.Adapter<UserCarHistoryAdapter.MyViewHolder> {

    private ArrayList<CarHistoryResponse> responseList;

    public UserCarHistoryAdapter(ArrayList<CarHistoryResponse> responseList) {
        this.responseList = responseList;
    }

    @NonNull
    @Override
    public UserCarHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserDashboardBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_user_dashboard, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCarHistoryAdapter.MyViewHolder holder, int position) {

        holder.binding.tvTitle.setText(responseList.get(position).getCarName());
        holder.binding.tvPrice.setText(responseList.get(position).getRatePerKM());
        holder.binding.tvSubtitle.setText(responseList.get(position).getAvailable());
        Picasso.get().load(responseList.get(position).getCarImage()).into(holder.binding.img);

    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemUserDashboardBinding binding;

        public MyViewHolder(@NonNull ItemUserDashboardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}


