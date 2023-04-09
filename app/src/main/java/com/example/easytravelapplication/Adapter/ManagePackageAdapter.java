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
import com.example.easytravelapplication.BuyNowActivity;
import com.example.easytravelapplication.Model.PackageListResponse;
import com.example.easytravelapplication.R;
import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.databinding.ListItemManagePackageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManagePackageAdapter extends RecyclerView.Adapter<ManagePackageAdapter.MyViewHolder> {
    private ArrayList<PackageListResponse> responseList;
    private Context context;

    public ManagePackageAdapter(ArrayList<PackageListResponse> responseList, Context ctx) {
        this.responseList = responseList;
        context = ctx;
    }

    @NonNull
    @Override
    public ManagePackageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemManagePackageBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item_manage_package, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ManagePackageAdapter.MyViewHolder holder, int position) {

        holder.itemRowBinding.tvPackageName.setText(responseList.get(position).getPackageName());
        holder.itemRowBinding.tvPlaces.setText(responseList.get(position).getPlaces());
        holder.itemRowBinding.tvDayNight.setText(responseList.get(position).getTotalDay() + "D/" + responseList.get(position).getTotalNight() + "N");
        //holder.itemRowBinding.tvPrice.setText(holder.itemView.getContext().getString(R.string.rupee) + responseList.get(position).getPrice() + "/Night");
        holder.itemRowBinding.tvPrice.setText(holder.itemView.getContext().getString(R.string.rupee) + responseList.get(position).getPrice());

        Picasso.get().load(responseList.get(position).getPackageImage()).into(holder.itemRowBinding.rivPackage);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (context.getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE).getString(AppConstant.USERTYPE, "").equals("User")) {
            holder.itemRowBinding.btnBuyNow.setVisibility(View.VISIBLE);
            holder.itemRowBinding.imgEdit.setVisibility(View.GONE);
            holder.itemRowBinding.deleterImg.setVisibility(View.GONE);

        } else {
            holder.itemRowBinding.btnBuyNow.setVisibility(View.GONE);
            holder.itemRowBinding.imgEdit.setVisibility(View.VISIBLE);
            holder.itemRowBinding.deleterImg.setVisibility(View.VISIBLE);
        }

        holder.itemRowBinding.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddManagePackageActivity.class);
                intent.putExtra("is_from_update", true);
                intent.putExtra("package_name", responseList.get(position).getPackageName());
                intent.putExtra("places", responseList.get(position).getPlaces());
                intent.putExtra("day", responseList.get(position).getTotalDay());
                intent.putExtra("night", responseList.get(position).getTotalNight());
                intent.putExtra("starting_date", responseList.get(position).getStartingDate());
                intent.putExtra("end_date", responseList.get(position).getEndDate());
                intent.putExtra("price", responseList.get(position).getPrice());
                intent.putExtra("image", responseList.get(position).getPackageImage());
                intent.putExtra("key", responseList.get(position).getKey());
                intent.putExtra("hotel", responseList.get(position).getHotelName());
                context.startActivity(intent);
            }
        });

        holder.itemRowBinding.btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BuyNowActivity.class);
                intent.putExtra("package_name", responseList.get(position).getPackageName());
                intent.putExtra("places", responseList.get(position).getPlaces());
                intent.putExtra("day", responseList.get(position).getTotalDay());
                intent.putExtra("night", responseList.get(position).getTotalNight());
                intent.putExtra("starting_date", responseList.get(position).getStartingDate());
                intent.putExtra("end_date", responseList.get(position).getEndDate());
                intent.putExtra("price", responseList.get(position).getPrice());
                intent.putExtra("image", responseList.get(position).getPackageImage());
                intent.putExtra("hotel_name", responseList.get(position).getHotelName());
                context.startActivity(intent);
            }
        });


        holder.itemRowBinding.deleterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("Do you want to Delete This Package ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Manage Packages");
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
        public ListItemManagePackageBinding itemRowBinding;

        public MyViewHolder(ListItemManagePackageBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;

        }
    }
}
