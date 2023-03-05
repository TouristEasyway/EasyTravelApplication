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
import android.view.MenuItem;
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
import com.example.easytravelapplication.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    DatabaseReference reference;
    StorageReference storageReference;
    FirebaseAuth auth;
    String image, uniqueKey, downloadURL;

    private static final int REQ = 123;
    private static final int STORAGE_CODE = 223;
    Bitmap bitmap;
    SharedPreferences sp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        requestStoragePermission();
        initView();
        initListener();
    }

    private void initListener() {
        binding.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        Picasso.get().load(image).into(binding.userProfileImage);

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidatation();
            }
        });
    }

    private void checkValidatation() {
        if (binding.TIEName.getText().toString().equals("")) {
            binding.TIEName.setError("Name is Required");
        } else if (binding.TIEEmail.getText().toString().matches("")) {
            binding.TIEEmail.setError("Email is Required");
        } else if (binding.TIEPhoneNo.getText().toString().equals("")) {
            binding.TIEPhoneNo.setError("Phone No is  Required");
        } else if (binding.radioGroup.getCheckedRadioButtonId() == -1) {
            new CommonMethod(ProfileActivity.this, "Please Select Gender");
        } else if (binding.TIEDob.getText().toString().equals("")) {
            binding.TIEDob.setError("Date of Birth is  Required");
        } else if (binding.TIECity.getText().toString().equals("")) {
            binding.TIECity.setError("City is  Required");
        } else if (binding.TIEState.getText().toString().equals("")) {
            binding.TIEState.setError("State is  Required");
        } else if (bitmap == null) {
            progressDialog.setMessage("Updating...");
            progressDialog.show();
            updateData(image);
        } else {
            progressDialog.setMessage("Updating...");
            progressDialog.show();
            uploadImage();
        }
    }

    private void uploadImage() {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] finalImage = byteArrayOutputStream.toByteArray();

        final StorageReference filePath;

        filePath = storageReference.child("Users").child(finalImage + "jpg");

        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void updateData(String s) {

        HashMap user = new HashMap<>();
        user.put("key", uniqueKey);
        user.put("name", binding.TIEName.getText().toString());
        user.put("email", binding.TIEEmail.getText().toString());
        user.put("contact_no", binding.TIEPhoneNo.getText().toString());
        user.put("gender", binding.rgMale.isChecked() ? "Male" : "Female");
        user.put("birth_date", binding.TIEDob.getText().toString());
        user.put("city", binding.TIECity.getText().toString());
        user.put("state", binding.TIEState.getText().toString());
        user.put("profile_pic", s);
        user.put("user_type", "User");
        user.put("status", "Active");

        reference.child(uniqueKey).updateChildren(user).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                progressDialog.dismiss();
                sp.edit().putString(AppConstant.USERTYPE, "User").commit();
                sp.edit().putString(AppConstant.EMAIL, binding.TIEEmail.getText().toString()).commit();
                sp.edit().putString(AppConstant.NAME, binding.TIEName.getText().toString()).commit();
                sp.edit().putString(AppConstant.DOB, binding.TIEDob.getText().toString()).commit();
                sp.edit().putString(AppConstant.CONTACT, binding.TIEPhoneNo.getText().toString()).commit();
                sp.edit().putString(AppConstant.CITY, binding.TIECity.getText().toString()).commit();
                sp.edit().putString(AppConstant.STATE, binding.TIEState.getText().toString()).commit();
                sp.edit().putString(AppConstant.GENDER,  binding.rgMale.isChecked() ? "Male" : "Female").commit();
                sp.edit().putString(AppConstant.PROFILE_IMAGE, s).commit();
                Toast.makeText(ProfileActivity.this, "Profile Updated Successfully.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
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
                binding.userProfileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);

        uniqueKey = sp.getString(AppConstant.KEY, "");
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        setData();
    }

    private void setData() {

        binding.tvUserName.setText(sp.getString(AppConstant.NAME, ""));
        binding.TIEName.setText(sp.getString(AppConstant.NAME, ""));
        binding.TIEEmail.setText(sp.getString(AppConstant.EMAIL, ""));
        binding.TIEPhoneNo.setText(sp.getString(AppConstant.CONTACT, ""));
        binding.TIEDob.setText(sp.getString(AppConstant.DOB, ""));
        binding.TIECity.setText(sp.getString(AppConstant.CITY, ""));
        binding.TIEState.setText(sp.getString(AppConstant.STATE, ""));

//                    uniqueKey = snapshot.child("key").getValue().toString();
//
        String genderName = sp.getString(AppConstant.GENDER, "");

        if (genderName.equals("Male")) {
            binding.rgMale.setChecked(true);
        } else {
            binding.rgFemale.setChecked(true);
        }
        String url = sp.getString(AppConstant.PROFILE_IMAGE, "");
        if (url != null && !url.isEmpty()) {
            Picasso.get().load(url).placeholder(R.drawable.ic_person).into(binding.userProfileImage);
        } else {
            binding.userProfileImage.setImageResource(R.drawable.ic_person);
        }
        progressDialog.dismiss();
//                }


//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        ProfileResponse data = snapshot.getValue(ProfileResponse.class);
//
//                        binding.tvUserName.setText(data.getName());
//                        binding.TIEName.setText(data.getName());
//                        binding.TIEEmail.setText(data.getEmail());
//                        binding.TIEPhoneNo.setText(data.getContact_no());
//                        binding.TIEDob.setText(data.getBirth_date());
//                        binding.TIECity.setText(data.getCity());
//                        binding.TIEState.setText(data.getState());
//
//                        uniqueKey = snapshot.child("key").getValue().toString();
//
//                        String genderName = data.getGender();
//
//                        if (genderName.equals("Male")) {
//                            binding.rgMale.setChecked(true);
//                        } else {
//                            binding.rgFemale.setChecked(true);
//                        }
//
//                        String url = data.getProfile_pic();
//                        if (url != null && !url.isEmpty()){
//                            Picasso.get().load(url).placeholder(R.drawable.ic_person).into(binding.userProfileImage);
//                        }else {
//                            binding.userProfileImage.setImageResource(R.drawable.ic_person);
//                        }
//                    }
//                    progressDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                progressDialog.dismiss();
//
//            }
//        });

//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
//                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
//
//                    binding.tvUserName.setText(snapshot.child(sp.getString(AppConstant.KEY, "")).child("name").getValue().toString());
//                    binding.TIEName.setText(snapshot.child(sp.getString(AppConstant.FIREBASE_CHILD_NAME, "")).child("name").getValue().toString());
//                    binding.TIEEmail.setText(snapshot.child(sp.getString(AppConstant.FIREBASE_CHILD_NAME, "")).child("email").getValue().toString());
//                    binding.TIEPhoneNo.setText(snapshot.child(sp.getString(AppConstant.FIREBASE_CHILD_NAME, "")).child("contact_no").getValue().toString());
//                    binding.TIEDob.setText(snapshot.child(sp.getString(AppConstant.FIREBASE_CHILD_NAME, "")).child("birth_date").getValue().toString());
//                    binding.TIECity.setText(snapshot.child(sp.getString(AppConstant.FIREBASE_CHILD_NAME, "")).child("city").getValue().toString());
//                    binding.TIEState.setText(snapshot.child(sp.getString(AppConstant.FIREBASE_CHILD_NAME, "")).child("state").getValue().toString());
//
////                    uniqueKey = snapshot.child("key").getValue().toString();
////
//                    String genderName = snapshot.child(sp.getString(AppConstant.FIREBASE_CHILD_NAME, "")).child("gender").getValue().toString();
//
//                    if (genderName.equals("Male")) {
//                        binding.rgMale.setChecked(true);
//                    } else {
//                        binding.rgFemale.setChecked(true);
//                    }
//
//                    String url = snapshot.child("profile_pic").getValue(String.class);
//                    if (url != null && !url.isEmpty()){
//                        Picasso.get().load(url).placeholder(R.drawable.ic_person).into(binding.userProfileImage);
//                    }else {
//                        binding.userProfileImage.setImageResource(R.drawable.ic_person);
//                    }
//                    progressDialog.dismiss();
////                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}