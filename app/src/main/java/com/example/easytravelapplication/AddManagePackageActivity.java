package com.example.easytravelapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.databinding.ActivityAddManagePackageBinding;
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

public class AddManagePackageActivity extends AppCompatActivity {

    ActivityAddManagePackageBinding binding;
    DatabaseReference reference, dbref;
    StorageReference storageReference;

    String image, uniqueKey, downloadURL, totalDay, totalNight;

    private static final int REQ = 123;
    private static final int STORAGE_CODE = 223;
    Bitmap bitmap;
    SharedPreferences sp;
    ProgressDialog progressDialog;

    boolean toUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_manage_package);

        getSupportActionBar().setTitle("Add Package");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestStoragePermission();
        initView();
        initListener();
    }

    private void initView() {
        Intent intent = getIntent();
        toUpdate = intent.getBooleanExtra("is_from_update", false);
        uniqueKey = intent.getStringExtra("key");
        binding.edtPackageName.setText(intent.getStringExtra("package_name"));
        binding.edtPlaces.setText(intent.getStringExtra("places"));
        binding.edtStartingDate.setText(intent.getStringExtra("starting_date"));
        totalDay = intent.getStringExtra("day");
        totalNight = intent.getStringExtra("night");
        binding.edtTotalDay.setText(totalDay);
        binding.edtTotalNight.setText(totalNight);
        binding.edtEndDate.setText(intent.getStringExtra("end_date"));
        binding.edtPrice.setText(intent.getStringExtra("price"));
        image = intent.getStringExtra("image");
        Picasso.get().load(image).placeholder(R.drawable.login_fi).into(binding.imgPackage);

        if (intent.getStringExtra("package_name") == null) {
            binding.btnManage.setText("Add Package");
            getSupportActionBar().setTitle(" Add Package");
        } else {
            binding.btnManage.setText("Update Package");
            getSupportActionBar().setTitle(" Update Package");
        }

        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);
        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(AddManagePackageActivity.this);
    }

    private void initListener() {
        binding.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        Picasso.get().load(image).placeholder(R.drawable.login_fi).into(binding.imgPackage);

        binding.btnManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_CODE);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ);
    }

    @Override
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                binding.imgPackage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void checkValidation() {
        if (binding.edtPackageName.getText().toString().equals("")) {
            binding.edtPackageName.setError("Package Name is Required");
        } else if (binding.edtPlaces.getText().toString().matches("")) {
            binding.edtPlaces.setError("Places are Required");
        } else if (binding.edtTotalDay.getText().toString().equals("")) {
            binding.edtTotalDay.setError("Total Day is  Required");
        } else if (binding.edtTotalNight.getText().toString().equals("")) {
            binding.edtTotalNight.setError("Total Night is  Required");
        } else if (binding.edtStartingDate.getText().toString().equals("")) {
            binding.edtStartingDate.setError("Starting Date Required");
        } else if (binding.edtEndDate.getText().toString().equals("")) {
            binding.edtEndDate.setError("End Date Required");
        } else if (binding.edtPrice.getText().toString().equals("")) {
            binding.edtPrice.setError("Price  Required");
        } else if (bitmap == null) {
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            updateData(image);
        } else {

            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            uploadImage();
        }
    }

    private void uploadImage() {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] finalImage = byteArrayOutputStream.toByteArray();

        final StorageReference filePath;

        filePath = storageReference.child("Manage Packages").child(finalImage + "jpg");

        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(AddManagePackageActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    updateData(downloadURL);
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(AddManagePackageActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void updateData(String s) {
        dbref = reference.child("Manage Packages");
        String key = reference.push().getKey();

        HashMap params = new HashMap<>();
        params.put("key", key);
        params.put("packageName", binding.edtPackageName.getText().toString());
        params.put("places", binding.edtPlaces.getText().toString());
        params.put("totalDay", binding.edtTotalDay.getText().toString());
        params.put("totalNight", binding.edtTotalNight.getText().toString());
        params.put("startingDate", binding.edtStartingDate.getText().toString());
        params.put("endDate", binding.edtEndDate.getText().toString());
        params.put("price", binding.edtPrice.getText().toString());
        params.put("packageImage", s);
        params.put("status", "Active");

        if (toUpdate) {
            dbref.child(uniqueKey).updateChildren(params).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    new CommonMethod(AddManagePackageActivity.this, "Package Updated Successfully.");
                    new CommonMethod(AddManagePackageActivity.this, ManagePackageActivity.class);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddManagePackageActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            dbref.child(key).setValue(params).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    progressDialog.dismiss();
                    Toast.makeText(AddManagePackageActivity.this, "Package Added Successfully.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddManagePackageActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            });
        }
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