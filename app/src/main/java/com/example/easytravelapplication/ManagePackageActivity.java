package com.example.easytravelapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Adapter.ManagePackageAdapter;
import com.example.easytravelapplication.Model.PackageListResponse;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.Utils.ConnectionDetector;
import com.example.easytravelapplication.databinding.ActivityManagePackageBinding;
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
    private   ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manage_package);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_package);
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
                        arrayList.add(data);
                    }

                    ManagePackageAdapter adapter = new ManagePackageAdapter(arrayList, ManagePackageActivity.this);
                    binding.rvPackage.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();

            }
        });
    }



    private void initListener() {
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonMethod(ManagePackageActivity.this, AddManagePackageActivity.class);
            }
        });
    }

}