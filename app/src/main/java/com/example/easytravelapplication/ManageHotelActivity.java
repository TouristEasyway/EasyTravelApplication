package com.example.easytravelapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Adapter.ManageHotelAdapter;
import com.example.easytravelapplication.Model.HotelListResponse;
import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.databinding.ActivityManageHotelBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageHotelActivity extends AppCompatActivity {

    ActivityManageHotelBinding binding;
    ArrayList<HotelListResponse> arrayList;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_hotel);
        getSupportActionBar().setTitle("Manage Hotel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initListener();
    }

    private void initView() {
        ProgressDialog pd = new ProgressDialog(ManageHotelActivity.this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        reference = FirebaseDatabase.getInstance().getReference("Manage Hotel");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HotelListResponse data = snapshot.getValue(HotelListResponse.class);
                        arrayList.add(data);
                    }
                    if (arrayList.isEmpty()) {
                        binding.rvPackage.setVisibility(View.GONE);
                        binding.tvNoData.setVisibility(View.VISIBLE);
                        pd.dismiss();
                    } else {
                        binding.tvNoData.setVisibility(View.GONE);
                        binding.rvPackage.setVisibility(View.VISIBLE);
                        ManageHotelAdapter adapter = new ManageHotelAdapter(arrayList, ManageHotelActivity.this);
                        binding.rvPackage.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        pd.dismiss();
                    }
                }
                else{
                    pd.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();

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
    private void initListener() {
         SharedPreferences sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);
         sp.getString(AppConstant.USERTYPE, "");
         String userType = sp.getString(AppConstant.USERTYPE, "");

        if (userType.equals("User")){
            binding.btnAdd.setVisibility(View.GONE);
        }
        else{
            binding.btnAdd.setVisibility(View.VISIBLE);
        }
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonMethod(ManageHotelActivity.this, AddManageHotelActivity.class);
            }
        });

    }
}