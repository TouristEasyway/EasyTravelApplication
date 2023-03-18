package com.example.easytravelapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.easytravelapplication.Model.CarHistoryResponse;
import com.example.easytravelapplication.Utils.ConnectionDetector;
import com.example.easytravelapplication.databinding.ActivityCarHistoryDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CarHistoryDetailActivity extends AppCompatActivity {

    ActivityCarHistoryDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_history_detail);
        getSupportActionBar().setTitle("Car History Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        updateUI((CarHistoryResponse) getIntent().getSerializableExtra("CAR_HISTORY_RESPONSE"));

    }

    private void updateUI(CarHistoryResponse carHistoryResponse) {

        binding.txtCarName.setText(carHistoryResponse.getCarName());
        binding.txtCarType.setText(carHistoryResponse.getCarType());
        binding.txtFuelType.setText(carHistoryResponse.getFuelType());
        binding.txtRate.setText(carHistoryResponse.getRatePerKM());
        binding.txtAvailable.setText(carHistoryResponse.getAvailable());
        binding.txtStartDate.setText(carHistoryResponse.getStartDate());
        binding.txtEndDate.setText(carHistoryResponse.getEndDate());
        if (carHistoryResponse.getCarImage() != null) {
            Picasso.get().load(carHistoryResponse.getCarImage()).placeholder(R.drawable.car).into(binding.imgCar);
        } else {
            Picasso.get().load(R.drawable.car).into(binding.imgCar);
        }
    }
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