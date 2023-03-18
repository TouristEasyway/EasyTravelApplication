package com.example.easytravelapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Adapter.PackageHistoryAdapter;
import com.example.easytravelapplication.Model.PackageHistoryListResponse;
import com.example.easytravelapplication.Utils.ConnectionDetector;
import com.example.easytravelapplication.databinding.ActivityPackagePurchaseHistoryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PackagePurchaseHistoryActivity extends AppCompatActivity {

    ActivityPackagePurchaseHistoryBinding binding;
    ArrayList<PackageHistoryListResponse> arrayList;
    DatabaseReference reference;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_package_purchase_history);

        getSupportActionBar().setTitle("Package History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (new ConnectionDetector(this).isConnectingToInternet()) {
            pd = new ProgressDialog(this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
            getPackageHistoryData();
        } else {
            new ConnectionDetector(this).connectiondetect();
        }

    }

    private void getPackageHistoryData() {
        reference = FirebaseDatabase.getInstance().getReference("Book Packages");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PackageHistoryListResponse data = snapshot.getValue(PackageHistoryListResponse.class);
                        if (data != null){
                            arrayList.add(data);
                            PackageHistoryAdapter adapter = new PackageHistoryAdapter(arrayList, PackagePurchaseHistoryActivity.this);
                            binding.rvPackage.setAdapter(adapter);
                            binding.rvPackage.setVisibility(View.VISIBLE);
                            binding.noData.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                            pd.dismiss();
                        }
                        else{
                            pd.dismiss();
                            binding.rvPackage.setVisibility(View.GONE);
                            binding.noData.setVisibility(View.VISIBLE);

                        }
                    }

                } else{
                        pd.dismiss();
                        binding.rvPackage.setVisibility(View.GONE);
                        binding.noData.setVisibility(View.VISIBLE);
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();
                binding.rvPackage.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
                Toast.makeText(PackagePurchaseHistoryActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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