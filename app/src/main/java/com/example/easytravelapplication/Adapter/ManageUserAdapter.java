package com.example.easytravelapplication.Adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytravelapplication.Model.LogInResponse;
import com.example.easytravelapplication.R;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.databinding.ItemManageUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ManageUserAdapter extends RecyclerView .Adapter<ManageUserAdapter.MyViewHolder>{

    private ArrayList<LogInResponse> dataModelList;
    private Context context;


    public ManageUserAdapter(Context ctx,ArrayList<LogInResponse> dataModelList) {
        this.dataModelList = dataModelList;
        this.context = ctx;

    }
    @NonNull
    @Override
    public ManageUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemManageUserBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_manage_user, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageUserAdapter.MyViewHolder holder, int position) {

        holder.itemRowBinding.tvName.setText(dataModelList.get(position).getName());
        holder.itemRowBinding.tvEmail.setText(dataModelList.get(position).getemail());
        holder.itemRowBinding.tvContactNo.setText(dataModelList.get(position).getcontact_no());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        holder.itemRowBinding.deleterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("Do you want to close this application ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                String childName = dataModelList.get(position).getemail().split("@")[0];
                                ProgressDialog pd = new ProgressDialog(context);
                                pd.setMessage("Please Wait...");
                                pd.setCancelable(false);
                                pd.show();

                                reference.child(childName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        pd.dismiss();
                                        new CommonMethod(context, "User Deleted Successfully");

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
        return dataModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ItemManageUserBinding itemRowBinding;

        public MyViewHolder(ItemManageUserBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;

        }
    }
}
