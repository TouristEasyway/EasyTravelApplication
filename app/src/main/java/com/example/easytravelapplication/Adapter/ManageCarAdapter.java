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

import com.example.easytravelapplication.AddCarActivity;
import com.example.easytravelapplication.BuyCarActivity;
import com.example.easytravelapplication.Model.ManageCarResponse;
import com.example.easytravelapplication.R;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.databinding.ItemManageCarBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManageCarAdapter extends RecyclerView.Adapter<ManageCarAdapter.ViewHolder> {
    private ArrayList<ManageCarResponse> responseList;
    private Context context;

    private String userType;

    public ManageCarAdapter(Context context, ArrayList<ManageCarResponse> responseList, String userType) {
        this.responseList = responseList;
        this.context = context;
        this.userType = userType;

    }

    @NonNull
    @Override
    public ManageCarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemManageCarBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_manage_car, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageCarAdapter.ViewHolder holder, int position) {

        holder.itemRowBinding.tvCarName.setText(responseList.get(position).getCarName());
        holder.itemRowBinding.tvFuelType.setText(responseList.get(position).getfuelType());
        holder.itemRowBinding.tvRateParKm.setText(responseList.get(position).getRatePerKM() + "Km/Hr");
        holder.itemRowBinding.tvAvailable.setText(responseList.get(position).getAvailable());

        if (responseList.get(position).getCarImage() == null) {
            Picasso.get().load(R.drawable.car).into(holder.itemRowBinding.imgCar);
        } else {
            Picasso.get().load(responseList.get(position).getCarImage()).into(holder.itemRowBinding.imgCar);
        }

        if (userType.equals("User")) {
            holder.itemRowBinding.deleterImg.setVisibility(View.GONE);
            holder.itemRowBinding.editImg.setVisibility(View.GONE);
            if (responseList.get(position).getAvailable().equals("Booked")) {
                holder.itemRowBinding.tvPurchase.setVisibility(View.GONE);
            } else {
                holder.itemRowBinding.tvPurchase.setVisibility(View.VISIBLE);

            }

        } else {
            holder.itemRowBinding.deleterImg.setVisibility(View.VISIBLE);
            holder.itemRowBinding.editImg.setVisibility(View.VISIBLE);
            holder.itemRowBinding.tvPurchase.setVisibility(View.GONE);

        }
        holder.itemRowBinding.tvPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BuyCarActivity.class);
                intent.putExtra("CAR_RESPONSE", responseList.get(position));
                context.startActivity(intent);

            }
        });
        holder.itemRowBinding.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddCarActivity.class);
                intent.putExtra("CAR_RESPONSE", responseList.get(position));
                intent.putExtra("UPDATE_CAR", true);
                intent.putExtra("image", responseList.get(position).getCarImage());
                context.startActivity(intent);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        holder.itemRowBinding.deleterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("Do you want to Delete Car ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Manage Car");
                                String childName = responseList.get(position).getKey();
                                ProgressDialog pd = new ProgressDialog(context);
                                pd.setMessage("Please Wait...");
                                pd.setCancelable(false);
                                pd.show();

                                reference.child(childName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        pd.dismiss();
                                        new CommonMethod(context, "Car Deleted Successfully");

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemManageCarBinding itemRowBinding;

        public ViewHolder(ItemManageCarBinding itemView) {
            super(itemView.getRoot());
            this.itemRowBinding = itemView;
        }
    }
}
