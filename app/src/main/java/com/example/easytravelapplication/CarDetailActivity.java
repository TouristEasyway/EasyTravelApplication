package com.example.easytravelapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Model.ManageCarResponse;
import com.example.easytravelapplication.databinding.ActivityCarDetailBinding;
import com.squareup.picasso.Picasso;

public class CarDetailActivity extends AppCompatActivity {

    ActivityCarDetailBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_detail);

        getSupportActionBar().setTitle("Car Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ManageCarResponse response = (ManageCarResponse) getIntent().getSerializableExtra("CAR_RESPONSE");
        updateUI(response);

    }


    private void updateUI(ManageCarResponse manageCarResponse) {
        binding.txtCarName.setText(manageCarResponse.getCarName());
        binding.txtCarType.setText(manageCarResponse.getCarType());
        binding.txtFuelType.setText(manageCarResponse.getfuelType());
        binding.txtRate.setText(manageCarResponse.getRatePerKM());
        binding.txtAvailable.setText(manageCarResponse.getAvailable());
        if (manageCarResponse.getCarImage() != null) {
            Picasso.get().load(manageCarResponse.getCarImage()).placeholder(R.drawable.car).into(binding.imgCar);
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