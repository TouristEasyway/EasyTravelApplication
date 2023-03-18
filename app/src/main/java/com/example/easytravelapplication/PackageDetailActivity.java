package com.example.easytravelapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Model.PackageListResponse;
import com.example.easytravelapplication.databinding.ActivityPackageDetailBinding;
import com.squareup.picasso.Picasso;

public class PackageDetailActivity extends AppCompatActivity {

    ActivityPackageDetailBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_package_detail);

        getSupportActionBar().setTitle("Package Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        updateUI((PackageListResponse) getIntent().getSerializableExtra("PACKAGE_RESPONSE"));
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

    private void updateUI(PackageListResponse packageListResponse) {
        binding.txtPackageName.setText(packageListResponse.getPackageName());
        binding.txtHotel.setText(packageListResponse.getHotelName());
        binding.txtPlaces.setText(packageListResponse.getPlaces());
        binding.txtPrice.setText(packageListResponse.getPrice());
        if (packageListResponse.getPackageImage() != null) {
            Picasso.get().load(packageListResponse.getPackageImage()).placeholder(R.drawable.car).into(binding.imgPackage);
        } else {
            Picasso.get().load(R.drawable.car).into(binding.imgPackage);
        }
    }
}