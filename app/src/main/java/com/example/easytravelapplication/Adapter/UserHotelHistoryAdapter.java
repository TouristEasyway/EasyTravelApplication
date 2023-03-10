
package com.example.easytravelapplication.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytravelapplication.Model.HotelHistoryListResponse;
import com.example.easytravelapplication.R;
import com.example.easytravelapplication.databinding.ItemUserDashboardBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class UserHotelHistoryAdapter extends RecyclerView.Adapter<UserHotelHistoryAdapter.MyViewHolder> {

    private ArrayList<HotelHistoryListResponse> responseList;

    public UserHotelHistoryAdapter(ArrayList<HotelHistoryListResponse> responseList) {
        this.responseList = responseList;
    }

    @NonNull
    @Override
    public UserHotelHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserDashboardBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_user_dashboard, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHotelHistoryAdapter.MyViewHolder holder, int position) {

        holder.binding.tvTitle.setText(responseList.get(position).getHotelName());
        holder.binding.tvPrice.setText(responseList.get(position).getPrice());
        holder.binding.tvSubtitle.setText(responseList.get(position).getAddress());
        Picasso.get().load(responseList.get(position).getHotelImage()).into(holder.binding.img);

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


