package com.example.easytravelapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Model.PackageHistoryListResponse;
import com.example.easytravelapplication.databinding.ActivityPackageHistoryBinding;
import com.squareup.picasso.Picasso;

public class PackageHistoryDetailActivity extends AppCompatActivity {


    ActivityPackageHistoryBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_package_history);

        getSupportActionBar().setTitle("Package History Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        updateUI((PackageHistoryListResponse) getIntent().getSerializableExtra("PACKAGE_HISTORY_RESPONSE"));
    }


    private void updateUI(PackageHistoryListResponse packageListResponse) {
        binding.txtPackageName.setText(packageListResponse.getPackageName());
        binding.txtHotel.setText(packageListResponse.getHotelName());
        binding.txtPlaces.setText(packageListResponse.getPlaces());
        binding.txtPrice.setText(getString(R.string.rupee)+""+packageListResponse.getPrice());
        binding.txtStartDate.setText(packageListResponse.getStartingDate());
        binding.txtEndDate.setText(packageListResponse.getEndDate());
        binding.txtPurchaseDate.setText(packageListResponse.getPurchaseDate());


        if (packageListResponse.getPackageImage() != null) {
            Picasso.get().load(packageListResponse.getPackageImage()).placeholder(R.drawable.car).into(binding.imgPackage);
        } else {
            Picasso.get().load(R.drawable.car).into(binding.imgPackage);
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