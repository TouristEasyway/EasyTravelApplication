package com.example.easytravelapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Adapter.ManagePackageAdapter;
import com.example.easytravelapplication.Model.PackageListResponse;
import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.Utils.ConnectionDetector;
import com.example.easytravelapplication.databinding.ActivityManagePackageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagePackageActivity extends AppCompatActivity {

    ActivityManagePackageBinding binding;
    ArrayList<PackageListResponse> arrayList;
    DatabaseReference reference;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manage_package);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_package);

        binding.bottomNavigationView.setSelectedItemId(R.id.package1);
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.package1:
                        return true;
                    case R.id.car:
                        startActivity(new Intent(getApplicationContext(), ManageCarRentActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        getSupportActionBar().setTitle("Manage Package");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (new ConnectionDetector(this).isConnectingToInternet()) {
            pd = new ProgressDialog(this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
            getPackageData();
        } else {
            new ConnectionDetector(this).connectiondetect();
        }
        initListener();
    }

    private void getPackageData() {
        reference = FirebaseDatabase.getInstance().getReference("Manage Packages");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PackageListResponse data = snapshot.getValue(PackageListResponse.class);
                        if (data != null) {
                            arrayList.add(data);
                            ManagePackageAdapter adapter = new ManagePackageAdapter(arrayList, ManagePackageActivity.this);
                            binding.rvPackage.setVisibility(View.VISIBLE);
                            binding.noData.setVisibility(View.GONE);
                            binding.rvPackage.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            pd.dismiss();
                        } else {
                            binding.rvPackage.setVisibility(View.GONE);
                            binding.noData.setVisibility(View.VISIBLE);
                            pd.dismiss();

                        }
                    }


                } else {
                    binding.rvPackage.setVisibility(View.GONE);
                    binding.noData.setVisibility(View.VISIBLE);
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.noData.setVisibility(View.VISIBLE);
                binding.rvPackage.setVisibility(View.GONE);
                pd.dismiss();

            }
        });
    }


    private void initListener() {
        SharedPreferences sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);
        sp.getString(AppConstant.USERTYPE, "");
        String userType = sp.getString(AppConstant.USERTYPE, "");
        if (userType.equals("User")) {
            binding.btnAdd.setVisibility(View.GONE);
        } else {
            binding.btnAdd.setVisibility(View.VISIBLE);
        }
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonMethod(ManagePackageActivity.this, AddManagePackageActivity.class);
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