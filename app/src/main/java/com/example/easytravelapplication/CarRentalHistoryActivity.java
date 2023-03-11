package com.example.easytravelapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Adapter.BuyCarAdapter;
import com.example.easytravelapplication.Model.CarHistoryResponse;
import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.Utils.ConnectionDetector;
import com.example.easytravelapplication.databinding.ActivityCarRentalHistoryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CarRentalHistoryActivity extends AppCompatActivity {

    ActivityCarRentalHistoryBinding binding;
    ArrayList<CarHistoryResponse> arrayList;
    DatabaseReference reference;
    private  String userType;
    private ProgressDialog pd;

    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_rental_history);


        getSupportActionBar().setTitle("Car Rental History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);
        sp.getString(AppConstant.USERTYPE, "");
        userType = sp.getString(AppConstant.USERTYPE, "");
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

        reference = FirebaseDatabase.getInstance().getReference("Book Car");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CarHistoryResponse data = snapshot.getValue(CarHistoryResponse.class);
                        arrayList.add(data);
                    }

                    BuyCarAdapter adapter = new BuyCarAdapter(arrayList, CarRentalHistoryActivity.this,userType);
                    binding.rvBuyCar.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();
                new CommonMethod(CarRentalHistoryActivity.this, "Something went Wrong");
            }
        });
    }

}