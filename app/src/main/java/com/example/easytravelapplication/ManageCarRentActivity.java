package com.example.easytravelapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.easytravelapplication.Adapter.ManageCarAdapter;
import com.example.easytravelapplication.Adapter.ManageUserAdapter;
import com.example.easytravelapplication.Model.LogInResponse;
import com.example.easytravelapplication.Model.ManageCarResponse;
import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.Utils.ConnectionDetector;
import com.example.easytravelapplication.databinding.ActivityManageCarRentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageCarRentActivity extends AppCompatActivity {


    ActivityManageCarRentBinding binding;

    ArrayList<ManageCarResponse> arrayList;
    ProgressDialog pd;
    SharedPreferences sp;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_car_rent);
        getSupportActionBar().setTitle("Manage Car");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);
        userType = sp.getString(AppConstant.USERTYPE, "");

        if (userType.equals("User")){
            binding.btnAdd.setVisibility(View.GONE);
        }
        else{
            binding.btnAdd.setVisibility(View.VISIBLE);
        }
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(ManageCarRentActivity.this, AddCarActivity.class);
            }
        });

//        pd = new ProgressDialog(this);
//        pd.setMessage("Please Wait...");
//        pd.setCancelable(false);
//        pd.show();
//        getCarData();


        if (new ConnectionDetector(this).isConnectingToInternet()) {
            pd = new ProgressDialog(this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
            getCarData();
        } else {
            new ConnectionDetector(this).connectiondetect();
        }
    }

    private void getCarData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Manage Car");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ManageCarResponse data = snapshot.getValue(ManageCarResponse.class);

                        if (data != null){
                            arrayList.add(data);
                            ManageCarAdapter adapter = new ManageCarAdapter(ManageCarRentActivity.this, arrayList, userType);
                            binding.rvCarData.setAdapter(adapter);
                            binding.rvCarData.setVisibility(View.VISIBLE);
                            binding.noData.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                            pd.dismiss();

                        }
                        else{
                            binding.rvCarData.setVisibility(View.GONE);
                            binding.noData.setVisibility(View.VISIBLE);
                            pd.dismiss();
                        }
                    }


                }
                else{
                    binding.rvCarData.setVisibility(View.GONE);
                    binding.noData.setVisibility(View.VISIBLE);
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                binding.rvCarData.setVisibility(View.GONE);
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