package com.example.easytravelapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Adapter.ManageCarAdapter;
import com.example.easytravelapplication.Adapter.ManagePackageAdapter;
import com.example.easytravelapplication.Model.ManageCarResponse;
import com.example.easytravelapplication.Model.PackageListResponse;
import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.databinding.ActivitySearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    ArrayList<ManageCarResponse> carArrayList;
    ArrayList<PackageListResponse> packageArrayList;
    ProgressDialog pd;
    SharedPreferences sp;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        getSupportActionBar().setTitle("Global Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);
        userType = sp.getString(AppConstant.USERTYPE, "");
        binding.edtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Override onQueryTextSubmit method which is call when submit query is searched
            @Override
            public boolean onQueryTextSubmit(String query) {
                pd = new ProgressDialog(SearchActivity.this);
                pd.setMessage("Please Wait...");
                pd.setCancelable(false);
                pd.show();
                getCarData(query);
                getPackageData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getPackageData(String query) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Manage Packages");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    packageArrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PackageListResponse data = snapshot.getValue(PackageListResponse.class);
                        if (data != null) {
                            if (Integer.parseInt(query) >= Integer.parseInt(data.getPrice())) {
                                packageArrayList.add(data);
                                ManagePackageAdapter adapter = new ManagePackageAdapter(packageArrayList, SearchActivity.this);
                                binding.rvPackage.setVisibility(View.VISIBLE);
                                binding.noPackageData.setVisibility(View.GONE);
                                binding.rvPackage.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                            else {
                                packageArrayList.remove(data);
                            }
                            pd.dismiss();
                        } else {
                            binding.rvPackage.setVisibility(View.GONE);
                            binding.noPackageData.setVisibility(View.VISIBLE);
                            pd.dismiss();
                        }
                    }
                } else {
                    binding.rvPackage.setVisibility(View.GONE);
                    binding.noPackageData.setVisibility(View.VISIBLE);
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.noPackageData.setVisibility(View.VISIBLE);
                binding.rvPackage.setVisibility(View.GONE);
                pd.dismiss();
            }
        });
    }

    private void getCarData(String query) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Manage Car");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    carArrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ManageCarResponse data = snapshot.getValue(ManageCarResponse.class);
                        if (data != null) {
                            if (Integer.parseInt(query) >= Integer.parseInt(data.getRatePerKM())) {
                                carArrayList.add(data);
                                ManageCarAdapter adapter = new ManageCarAdapter(SearchActivity.this, carArrayList, userType);
                                binding.rvCar.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                binding.rvCar.setVisibility(View.VISIBLE);
                                binding.noCarData.setVisibility(View.GONE);
                            } else {
                                carArrayList.remove(data);
                            }

                            pd.dismiss();
                        } else {
                            binding.rvCar.setVisibility(View.GONE);
                            binding.noCarData.setVisibility(View.VISIBLE);
                            pd.dismiss();
                        }
                    }
                } else {
                    binding.rvCar.setVisibility(View.GONE);
                    binding.noCarData.setVisibility(View.VISIBLE);
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.rvCar.setVisibility(View.GONE);
                binding.noCarData.setVisibility(View.VISIBLE);
                pd.dismiss();
            }
        });
    }
}