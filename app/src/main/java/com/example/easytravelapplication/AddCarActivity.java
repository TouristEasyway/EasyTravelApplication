package com.example.easytravelapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.easytravelapplication.Model.ManageCarResponse;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.Utils.ConnectionDetector;
import com.example.easytravelapplication.databinding.ActivityAddCarBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class AddCarActivity extends AppCompatActivity {

    ActivityAddCarBinding binding;
    DatabaseReference reference, dbref;
    String uniqueKey;
    ProgressDialog pd;
    ManageCarResponse response;

    boolean toUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_car);

       response  = (ManageCarResponse) getIntent().getSerializableExtra("CAR_RESPONSE");
        toUpdate = getIntent().getBooleanExtra("UPDATE_CAR", false);

        if (toUpdate){
            binding.btnAddCar.setText("Update Car");
            setDataForUpdate(response);

        }
        else{
            binding.btnAddCar.setText("Add Car");
        }



        pd = new ProgressDialog(AddCarActivity.this);
        reference = FirebaseDatabase.getInstance().getReference();


        binding.btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateDetails();
            }
        });
    }

    private void setDataForUpdate(ManageCarResponse response) {
        binding.edtCarName.setText(response.getCarName());

        if (response.getFurlType().equals("Petrol")){
            binding.rbPetrol.setChecked(true);
        }
        else{
            binding.rbCNG.setChecked(true);
        }

        if (response.getCarType().equals("Auto")){
            binding.rbAuto.setChecked(true);
        }
        else{
            binding.rbManul.setChecked(true);

        }
        binding.edtRatePerKm.setText(response.getRatePerKM());
        binding.edtCity.setText(response.getCity());
        binding.edtState.setText(response.getState());
        binding.edtAvailable.setText(response.getAvailable());




    }

    private void validateDetails() {
        if (binding.edtCarName.getText().equals("")){
            binding.edtCarName.setError("Please Enter Car Name");
        }
        else if (binding.radioGroupFuelTye.getCheckedRadioButtonId() == -1){
            new CommonMethod(AddCarActivity.this, "Select Fuel Type");
        }
        else if (binding.radioGroupCarTye.getCheckedRadioButtonId() == -1){
            new CommonMethod(AddCarActivity.this, "Select Car Type");
        }
        else if (binding.edtRatePerKm.getText().equals("")){
            binding.edtRatePerKm.setError("Please Enter rate par km");
        }

        else if (binding.edtAvailable.getText().equals("")){
            binding.edtAvailable.setError("Please Car availability");

        }
        else{
//            pd= new ProgressDialog(AddCarActivity.this);
//            pd.setMessage("Please Wait...");
//            pd.setCancelable(false);
//            pd.show();


            if (new ConnectionDetector(this).isConnectingToInternet()) {
                pd = new ProgressDialog(this);
                pd.setMessage("Please Wait...");
                pd.setCancelable(false);
                pd.show();
                addCarData();
            } else {
                new ConnectionDetector(this).connectiondetect();
            }
        }
    }

    private void addCarData() {
        dbref = reference.child("Manage Car");
        String key = reference.push().getKey();


        HashMap params = new HashMap<>();
        params.put("key", key);
        params.put("carName", binding.edtCarName.getText().toString());
        params.put("furlType", binding.rbPetrol.isChecked() ? "Petrol" : "CNG");
        params.put("carType", binding.rbAuto.isChecked() ? "Auto" : "Manual");
        params.put("ratePerKM", binding.edtRatePerKm.getText().toString());
        params.put("city", binding.edtCity.getText().toString());
        params.put("state", binding.edtState.getText().toString());
        params.put("available", binding.edtAvailable.getText().toString());


        if (toUpdate) {
            dbref.child(response.getKey()).updateChildren(params).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {

                    new CommonMethod(AddCarActivity.this,  "Car Updated");
                    new CommonMethod(AddCarActivity.this, ManageCarRentActivity.class);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddCarActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else{
            dbref.child(key).setValue(params).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    pd.dismiss();
                    Toast.makeText(AddCarActivity.this, "Car Data Added Successfully.", Toast.LENGTH_SHORT).show();
                    new CommonMethod(AddCarActivity.this, ManageCarRentActivity.class);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(AddCarActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}