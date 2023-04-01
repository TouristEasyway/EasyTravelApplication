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
import com.example.easytravelapplication.Model.PackageListResponse;
import com.example.easytravelapplication.PackageDetailActivity;
import com.example.easytravelapplication.R;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.databinding.ItemUserDashboardBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserDashBoardPackageAdapter extends RecyclerView.Adapter<UserDashBoardPackageAdapter.MyViewHolder> {

    private ArrayList<PackageListResponse> responseList;

    public UserDashBoardPackageAdapter(ArrayList<PackageListResponse> responseList) {
        this.responseList = responseList;
    }

    @NonNull
    @Override
    public UserDashBoardPackageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserDashboardBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_user_dashboard, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDashBoardPackageAdapter.MyViewHolder holder, int position) {

        holder.binding.tvTitle.setText(responseList.get(position).getPackageName());
        holder.binding.tvPrice.setText(holder.itemView.getContext().getString(R.string.rupee)+""+responseList.get(position).getPrice());
        holder.binding.tvSubtitle.setText(responseList.get(position).getPlaces());
        Picasso.get().load(responseList.get(position).getPackageImage()).into(holder.binding.img);

        holder.binding.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), PackageDetailActivity.class);
                intent.putExtra("PACKAGE_RESPONSE",responseList.get(position));
                holder.itemView.getContext().startActivity(intent);            }
        });

    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public class MyViewHolder extends   RecyclerView.ViewHolder{
        ItemUserDashboardBinding binding;
        public MyViewHolder(@NonNull ItemUserDashboardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
