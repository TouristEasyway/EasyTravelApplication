package com.example.easytravelapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.easytravelapplication.Adapter.ManageUserAdapter;
import com.example.easytravelapplication.Model.LogInResponse;
import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.ConnectionDetector;
import com.example.easytravelapplication.databinding.ActivityManageUserBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageUserActivity extends AppCompatActivity {

    ActivityManageUserBinding binding;
    ArrayList<LogInResponse> arrayList;
    ProgressDialog pd;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_manage_user);

        getSupportActionBar().setTitle("Manage Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();
        getUserData();


//        if (new ConnectionDetector(context).isConnectingToInternet()) {
//            pd = new ProgressDialog(context);
//            pd.setMessage("Please Wait...");
//            pd.setCancelable(false);
//            pd.show();
//           getUserData();
//        } else {
//            new ConnectionDetector(context).connectiondetect();
//        }

    }

    private void getUserData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        LogInResponse data = snapshot.getValue(LogInResponse.class);
                        if (data != null){
                            arrayList.add(data);
                            ManageUserAdapter adapter = new ManageUserAdapter(ManageUserActivity.this,arrayList);
                            binding.itemManageUser.setAdapter(adapter);
                            binding.itemManageUser.setVisibility(View.VISIBLE);
                            binding.noData.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                            pd.dismiss();
                        }
                        else{
                            binding.itemManageUser.setVisibility(View.GONE);
                            binding.noData.setVisibility(View.VISIBLE);
                            pd.dismiss();
                        }

                    }

                }
                else{
                    binding.itemManageUser.setVisibility(View.GONE);
                    binding.noData.setVisibility(View.VISIBLE);
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                binding.itemManageUser.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
                pd.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Use For Close Application
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}