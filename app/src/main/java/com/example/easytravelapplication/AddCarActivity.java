package com.example.easytravelapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Model.ManageCarResponse;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.Utils.ConnectionDetector;
import com.example.easytravelapplication.databinding.ActivityAddCarBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class AddCarActivity extends AppCompatActivity {

    ActivityAddCarBinding binding;
    DatabaseReference reference, dbref;
    String uniqueKey;
    ProgressDialog pd;
    ManageCarResponse response;

    StorageReference storageReference;

    String image, downloadURL;
    Bitmap bitmap;
    private static final int REQ = 123;
    private static final int STORAGE_CODE = 223;

    boolean toUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_car);

        getSupportActionBar().setTitle("Add Car");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestStoragePermission();
        response = (ManageCarResponse) getIntent().getSerializableExtra("CAR_RESPONSE");
        toUpdate = getIntent().getBooleanExtra("UPDATE_CAR", false);
        image = getIntent().getStringExtra("image");
        Picasso.get().load(image).placeholder(R.drawable.login_fi).into(binding.customImageview1);
        if (toUpdate) {
            binding.btnAddCar.setText("Update Car");
            setDataForUpdate(response);

        } else {
            binding.btnAddCar.setText("Add Car");
        }


        pd = new ProgressDialog(AddCarActivity.this);
        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        binding.cardImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        Picasso.get().load(image).placeholder(R.drawable.ic_camera).into(binding.customImageview1);

        binding.btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateDetails();
            }
        });
    }

    private void setDataForUpdate(ManageCarResponse response) {
        binding.edtCarName.setText(response.getCarName());

        if (response.getFurlType().equals("Petrol")) {
            binding.rbPetrol.setChecked(true);
        } else {
            binding.rbCNG.setChecked(true);
        }

        if (response.getCarType().equals("Auto")) {
            binding.rbAuto.setChecked(true);
        } else {
            binding.rbManul.setChecked(true);

        }
        binding.edtRatePerKm.setText(response.getRatePerKM());
        binding.edtCity.setText(response.getCity());
        binding.edtState.setText(response.getState());
        binding.edtAvailable.setText(response.getAvailable());

        if (response.getCarImage().isEmpty()) {
            Picasso.get().load(R.drawable.car).into(binding.customImageview1);
        } else {
            Picasso.get().load(image).into(binding.customImageview1);
        }
    }

    private void validateDetails() {
        if (binding.edtCarName.getText().equals("")) {
            binding.edtCarName.setError("Please Enter Car Name");
        } else if (binding.radioGroupFuelTye.getCheckedRadioButtonId() == -1) {
            new CommonMethod(AddCarActivity.this, "Select Fuel Type");
        } else if (binding.radioGroupCarTye.getCheckedRadioButtonId() == -1) {
            new CommonMethod(AddCarActivity.this, "Select Car Type");
        } else if (binding.edtRatePerKm.getText().equals("")) {
            binding.edtRatePerKm.setError("Please Enter rate par km");
        } else if (binding.edtAvailable.getText().equals("")) {
            binding.edtAvailable.setError("Please Car availability");
        }else if (bitmap == null) {
            if (new ConnectionDetector(this).isConnectingToInternet()) {
                pd = new ProgressDialog(this);
                pd.setMessage("Please Wait...");
                pd.setCancelable(false);
                pd.show();
                addCarData(image);
            } else {
                new ConnectionDetector(this).connectiondetect();
            }
        } else {
            if (new ConnectionDetector(this).isConnectingToInternet()) {
                pd = new ProgressDialog(this);
                pd.setMessage("Please Wait...");
                pd.setCancelable(false);
                pd.show();
                uploadImages();
            } else {
                new ConnectionDetector(this).connectiondetect();
            }
        }

    }

    private void addCarData(String image) {
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
        params.put("carImage", image);

        if (toUpdate) {
            dbref.child(response.getKey()).updateChildren(params).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    new CommonMethod(AddCarActivity.this, "Car Updated");
                    new CommonMethod(AddCarActivity.this, ManageCarRentActivity.class);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddCarActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
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

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Oops you just denied Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                binding.customImageview1.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImages() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] finalImage = byteArrayOutputStream.toByteArray();

        final StorageReference filePath;

        filePath = storageReference.child("Manage Car").child(finalImage + "jpg");

        final UploadTask uploadTask = filePath.putBytes(finalImage);

        uploadTask.addOnCompleteListener(AddCarActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadURL = String.valueOf(uri);
                                    addCarData(downloadURL);
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(AddCarActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }
        });


    }
    @Override
    public void onBackPressed() {
        //Use For Close Application
        finishAffinity();
    }
}