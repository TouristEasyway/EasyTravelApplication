package com.example.easytravelapplication.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytravelapplication.AddManagePackageActivity;
import com.example.easytravelapplication.BookHotelActivity;
import com.example.easytravelapplication.Model.HotelListResponse;
import com.example.easytravelapplication.R;
import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.databinding.ListItemManageHotelBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManageHotelAdapter extends RecyclerView.Adapter<ManageHotelAdapter.MyViewHolder> {
    private ArrayList<HotelListResponse> responseList;
    private Context context;

    public ManageHotelAdapter(ArrayList<HotelListResponse> responseList, Context ctx) {
        this.responseList = responseList;
        context = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemManageHotelBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item_manage_hotel, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        HotelListResponse response = responseList.get(position);

        holder.binding.tvHotelName.setText(response.getHotelName());
        holder.binding.tvPlaces.setText(response.getAddress() + ", " + response.getCity());
        holder.binding.tvPrice.setText(response.getPrice());

        Picasso.get().load(response.getHotelImages()).placeholder(R.drawable.ic_person).into(holder.binding.rivPackage);

        if (context.getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE).getString(AppConstant.USERTYPE, "").equals("User")) {
            holder.binding.btnBuyNow.setVisibility(View.VISIBLE);
            holder.binding.deleterImg.setVisibility(View.GONE);
            holder.binding.imgEdit.setVisibility(View.GONE);
        } else {
            holder.binding.btnBuyNow.setVisibility(View.GONE);
            holder.binding.deleterImg.setVisibility(View.VISIBLE);
            holder.binding.imgEdit.setVisibility(View.VISIBLE);
        }

        holder.binding.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddManagePackageActivity.class);
                intent.putExtra("hotel_name", response.getHotelName());
                intent.putExtra("address", response.getAddress());
                intent.putExtra("city", response.getCity());
                intent.putExtra("state", response.getState());
                intent.putExtra("location", response.getLocation());
                intent.putExtra("price", response.getPrice());
                intent.putExtra("check_in_time", response.getCheckInTime());
                intent.putExtra("check_out_time", response.getCheckOutTime());
                intent.putExtra("service", response.getService());
                intent.putExtra("hotel_image", response.getHotelImages());
                context.startActivity(intent);
            }
        });

        holder.binding.btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookHotelActivity.class);
                intent.putExtra("hotel_name", response.getHotelName());
                intent.putExtra("address", response.getAddress());
                intent.putExtra("city", response.getCity());
                intent.putExtra("state", response.getState());
                intent.putExtra("location", response.getLocation());
                intent.putExtra("price", response.getPrice());
                intent.putExtra("check_in_time", response.getCheckInTime());
                intent.putExtra("check_out_time", response.getCheckOutTime());
                intent.putExtra("service", response.getService());
                intent.putExtra("hotel_image", response.getHotelImages());
                context.startActivity(intent);
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        holder.binding.deleterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("Do you want to Delete This Package ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Manage Hotel");
                                ProgressDialog pd = new ProgressDialog(context);
                                pd.setMessage("Please Wait...");
                                pd.setCancelable(false);
                                pd.show();

                                reference.child(responseList.get(position).getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        pd.dismiss();
                                        new CommonMethod(context, "Package Deleted Successfully");
                                        notifyDataSetChanged();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        new CommonMethod(context, "Something went Wrong");
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("AlertDialogExample");
                alert.show();


            }
        });
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ListItemManageHotelBinding binding;

        public MyViewHolder(ListItemManageHotelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
