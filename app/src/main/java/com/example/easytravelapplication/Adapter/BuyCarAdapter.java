package com.example.easytravelapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytravelapplication.Model.CarHistoryResponse;
import com.example.easytravelapplication.R;
import com.example.easytravelapplication.databinding.ItemCarHistoryBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BuyCarAdapter  extends RecyclerView.Adapter<BuyCarAdapter.MyViewHolder> {

    private ArrayList<CarHistoryResponse> responseList;
    private Context context;
    private  String userType;



    public BuyCarAdapter(ArrayList<CarHistoryResponse> responseList, Context context, String userType) {
        this.responseList = responseList;
        this.context = context;
        this.userType = userType;
    }

    @NonNull
    @Override
    public BuyCarAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCarHistoryBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_car_history, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyCarAdapter.MyViewHolder holder, int position) {

        holder.binding.tvCarName.setText(responseList.get(position).getCarName());
        holder.binding.tvUserName.setText(responseList.get(position).getFullName());
        holder.binding.tvPrice.setText(responseList.get(position).getPrice());
        holder.binding.tvTotalKm.setText(responseList.get(position).getRatePerKM() + "KM/Hr");
        holder.binding.tvDay.setText(responseList.get(position).bookDate);
        if (responseList.get(position).getCarImage().equals("")){
            Picasso.get().load(R.drawable.car).into(holder.binding.imgCar);
        }
        else{
            Picasso.get().load(responseList.get(position).getCarImage()).into(holder.binding.imgCar);
        }

    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemCarHistoryBinding binding;
        public MyViewHolder(@NonNull ItemCarHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}