package com.example.easytravelapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Model.PackageListResponse;
import com.example.easytravelapplication.Utils.CommonMethod;
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
        binding.txtPrice.setText(getString(R.string.rupee)+""+packageListResponse.getPrice());
        binding.txtTotalDays.setText(packageListResponse.getTotalDay());
        binding.txtTotalNight.setText(packageListResponse.getTotalNight());
        if (packageListResponse.getPackageImage() != null) {
            Picasso.get().load(packageListResponse.getPackageImage()).placeholder(R.drawable.car).into(binding.imgPackage);
        } else {
            Picasso.get().load(R.drawable.car).into(binding.imgPackage);
        }

        binding.buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PackageDetailActivity.this, BuyNowActivity.class);
                intent.putExtra("package_name", packageListResponse.getPackageName());
                intent.putExtra("places",  packageListResponse.getPlaces());
                intent.putExtra("day",  packageListResponse.getTotalDay());
                intent.putExtra("night",  packageListResponse.getTotalNight());
                intent.putExtra("starting_date",  packageListResponse.getStartingDate());
                intent.putExtra("end_date",  packageListResponse.getEndDate());
                intent.putExtra("price",  packageListResponse.getPrice());
                intent.putExtra("image",  packageListResponse.getPackageImage());
                startActivity(intent);
            }

        });
    }
}