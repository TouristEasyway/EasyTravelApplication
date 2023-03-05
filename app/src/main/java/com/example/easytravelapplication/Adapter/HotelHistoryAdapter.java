package com.example.easytravelapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytravelapplication.Model.HotelHistoryListResponse;
import com.example.easytravelapplication.R;
import com.example.easytravelapplication.databinding.ListItemHotelHistoryBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HotelHistoryAdapter extends RecyclerView.Adapter<HotelHistoryAdapter.MyViewHolder> {
    private ArrayList<HotelHistoryListResponse> responseList;
    private Context context;

    public HotelHistoryAdapter(ArrayList<HotelHistoryListResponse> responseList, Context ctx) {
        this.responseList = responseList;
        context = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemHotelHistoryBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item_hotel_history, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        HotelHistoryListResponse response = responseList.get(position);

        holder.binding.tvUserName.setText(response.getUserName());
        holder.binding.tvHotelName.setText(response.getHotelName());
        holder.binding.tvStartingDate.setText(response.getCheckInDate());
        holder.binding.tvEndDate.setText(response.getCheckOutDate());

        Picasso.get().load(response.getHotelImage()).into(holder.binding.rivHotel);

        holder.binding.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String temp = "tel:" + response.getUserContact();
                intent.setData(Uri.parse(temp));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ListItemHotelHistoryBinding binding;

        public MyViewHolder(ListItemHotelHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
