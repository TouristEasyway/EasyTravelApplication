package com.example.easytravelapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Model.HotelListResponse;
import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.databinding.ActivityAddManagePackageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

    String hotelName;

    ArrayList<String> hotelList = new ArrayList<>();
    ArrayList<HotelListResponse> arrayList = new ArrayList<>();

    ArrayList<Integer> langList = new ArrayList<>();
    String langArray[] = new String[arrayList.size()];
    boolean[] selectedLanguage = new boolean[langArray.length];
    private ArrayList<String> cityList = new ArrayList<>();
    String city = "";

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

        if (intent.getStringExtra("package_name") == null) {
            binding.btnManage.setText("Add Package");
            getSupportActionBar().setTitle(" Add Package");
        } else {
            binding.btnManage.setText("Update Package");
            getSupportActionBar().setTitle(" Update Package");
            updateUI(intent);
        }

        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);
        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        cityList.add("City");
        cityList.add("Ahmedabad");
        cityList.add("Dahod");
        cityList.add("Surat");

        ArrayAdapter cityAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.citySpinner.setAdapter(cityAdapter);

        binding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (cityList.get(i).equalsIgnoreCase("City")) {
                    city = "";
                } else {
                    city = cityList.get(i);
                }
                if(city.equalsIgnoreCase("")){
                    binding.labelSpinner.setVisibility(View.GONE);
                    binding.hotels.setVisibility(View.GONE);
                }
                else {
                    binding.labelSpinner.setVisibility(View.VISIBLE);
                    binding.hotels.setVisibility(View.VISIBLE);
                    getHotelName(city);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        progressDialog = new ProgressDialog(AddManagePackageActivity.this);
    }

    private void updateUI(Intent intent) {
        uniqueKey = intent.getStringExtra("key");
        binding.edtPackageName.setText(intent.getStringExtra("package_name"));
        binding.edtPlaces.setText(intent.getStringExtra("places"));
//        binding.edtStartingDate.setText(intent.getStringExtra("starting_date"));
        totalDay = intent.getStringExtra("day");
        totalNight = intent.getStringExtra("night");
        binding.edtTotalDay.setText(totalDay);
        binding.edtTotalNight.setText(totalNight);
//        binding.edtEndDate.setText(intent.getStringExtra("end_date"));
        binding.edtPrice.setText(intent.getStringExtra("price"));
        image = intent.getStringExtra("image");
        String hotelName = intent.getStringExtra("hotel");
        binding.hotels.setText(hotelName);
        Picasso.get().load(image).placeholder(R.drawable.login_fi).into(binding.imgPackage);
    }

    private void initListener() {

//        ArrayAdapter hotelAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,hotelList);
//        hotelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        binding.hotels.setAdapter(hotelAdapter);
//
//        binding.hotels.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                hotelName = hotelList.get(i);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
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

    private void selectHotelPopup(String city) {
        binding.hotels.setOnClickListener(new View.OnClickListener() {
            boolean[] selectedLanguage;

            @Override
            public void onClick(View view) {
                ArrayList<Integer> langList = new ArrayList<>();
                String langArray[] = new String[arrayList.size()];
                selectedLanguage = new boolean[langArray.length];

                for (int i = 0; i < arrayList.size(); i++) {
                    if (city.equalsIgnoreCase(arrayList.get(i).getCity())) {
                        langArray[i] = arrayList.get(i).getHotelName();
                        //binding.hotels.setVisibility(View.VISIBLE);
                        //binding.labelSpinner.setVisibility(View.VISIBLE);
                    } else {
                        arrayList.remove(i);
                        //binding.hotels.setVisibility(View.GONE);
                        //binding.labelSpinner.setVisibility(View.GONE);
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(AddManagePackageActivity.this);

                // set title
                builder.setTitle("Select Hotel");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(langArray, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            langList.add(i);
                            // Sort array list
                            Collections.sort(langList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            langList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        // use for loop
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j = 0; j < langList.size(); j++) {
                            // concat array value
                            stringBuilder.append(langArray[langList.get(j)]);
                            // check condition
                            if (j != langList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        binding.hotels.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedLanguage.length; j++) {
                            // remove all selection
                            selectedLanguage[j] = false;
                            // clear language list
                            langList.clear();
                            // clear text view value
                            binding.hotels.setText("");
                        }
                    }
                });
                // show dialog
                builder.show();
            }
        });
    }

    private void getHotelName(String city) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Manage Hotel");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(String.valueOf(snapshot.child("city").getValue()).equalsIgnoreCase(city)) {
                            HotelListResponse data = snapshot.getValue(HotelListResponse.class);
                            arrayList.add(data);
                        }
//                        if (city.equalsIgnoreCase(data.getCity())) {
//                            arrayList.add(data);
//                            binding.hotels.setVisibility(View.VISIBLE);
//                            binding.labelSpinner.setVisibility(View.VISIBLE);
//                        } else {
//                            arrayList.remove(data);
//                        }
                    }
                    if(arrayList.size()<=0){
                        binding.labelSpinner.setVisibility(View.GONE);
                        binding.hotels.setVisibility(View.GONE);
                    }
                    else {
                        binding.labelSpinner.setVisibility(View.VISIBLE);
                        binding.hotels.setVisibility(View.VISIBLE);
                        selectHotelPopup(city);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
//        } else if (binding.edtStartingDate.getText().toString().equals("")) {
//            binding.edtStartingDate.setError("Starting Date Required");
//        } else if (binding.edtEndDate.getText().toString().equals("")) {
//            binding.edtEndDate.setError("End Date Required");
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
        params.put("city", city);
        params.put("places", binding.edtPlaces.getText().toString());
        params.put("totalDay", binding.edtTotalDay.getText().toString());
        params.put("totalNight", binding.edtTotalNight.getText().toString());
//        params.put("startingDate", binding.edtStartingDate.getText().toString());
//        params.put("endDate", binding.edtEndDate.getText().toString());
        params.put("price", binding.edtPrice.getText().toString());
        params.put("packageImage", s);
        params.put("status", "Active");
        params.put("hotelName", binding.hotels.getText().toString());

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
        } else {
            dbref.child(key).setValue(params).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    progressDialog.dismiss();
                    Toast.makeText(AddManagePackageActivity.this, "Package Added Successfully.", Toast.LENGTH_SHORT).show();
                    new CommonMethod(AddManagePackageActivity.this, ManagePackageActivity.class);
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