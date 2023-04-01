
package com.example.easytravelapplication.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytravelapplication.CarDetailActivity;
import com.example.easytravelapplication.CarHistoryDetailActivity;
import com.example.easytravelapplication.Model.CarHistoryResponse;
import com.example.easytravelapplication.PackageDetailActivity;
import com.example.easytravelapplication.R;
import com.example.easytravelapplication.Utils.CommonMethod;
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
        holder.binding.tvPrice.setText(responseList.get(position).getRatePerKM() + "Km/Hr");
        holder.binding.tvSubtitle.setText(responseList.get(position).getAvailable());
        if (responseList.get(position).getCarImage() != null) {
            Picasso.get().load(responseList.get(position).getCarImage()).placeholder(R.drawable.car).into(holder.binding.img);
        } else {
            Picasso.get().load(R.drawable.car).into(holder.binding.img);
        }
        holder.binding.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(),CarHistoryDetailActivity.class);
                intent.putExtra("CAR_HISTORY_RESPONSE",responseList.get(position));
                holder.itemView.getContext().startActivity(intent);
            }
        });
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


