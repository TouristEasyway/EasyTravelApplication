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
        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);
        sp.getString(AppConstant.USERTYPE, "");
        userType = sp.getString(AppConstant.USERTYPE, "");

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
                if (dataSnapshot.exists()){

                    arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ManageCarResponse data = snapshot.getValue(ManageCarResponse.class);
                        arrayList.add(data);
                    }
                    pd.dismiss();

                    ManageCarAdapter adapter = new ManageCarAdapter( ManageCarRentActivity.this,arrayList,userType);
                    binding.rvCarData.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                pd.dismiss();
            }
        });


    }
}