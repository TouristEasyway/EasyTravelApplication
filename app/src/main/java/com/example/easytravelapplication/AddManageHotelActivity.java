package com.example.easytravelapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TimePicker;
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
import com.example.easytravelapplication.databinding.ActivityAddManageHotelBinding;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AddManageHotelActivity extends AppCompatActivity {

    ActivityAddManageHotelBinding binding;
    DatabaseReference reference, dbref;
    StorageReference storageReference;

    String image, downloadURL, uniqueKey;
    Bitmap bitmap;
    SharedPreferences sp;
    ProgressDialog progressDialog;
    private static final int REQ = 123;
    private static final int STORAGE_CODE = 223;
    //    int IMAGE_CODE = 123;
    ArrayList<Uri> arrayList;
    ArrayList<Uri> selectedImageList;
    //    ImageAdapter adapter;
    boolean toUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_manage_hotel);

        getSupportActionBar().setTitle("Add Hotel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestStoragePermission();
        initView();
        initListener();
    }

    private void initView() {
        Intent intent = getIntent();
        toUpdate = intent.getBooleanExtra("is_from_update", false);
        uniqueKey = intent.getStringExtra("key");
        binding.edtHotelName.setText(intent.getStringExtra("hotel_name"));
        binding.edtAddress.setText(intent.getStringExtra("address"));
        binding.edtCity.setText(intent.getStringExtra("city"));
        binding.edtState.setText(intent.getStringExtra("state"));
        binding.edtLocation.setText(intent.getStringExtra("location"));
        binding.edtPrice.setText(intent.getStringExtra("price"));
        binding.edtCheckInTime.setText(intent.getStringExtra("check_in_time"));
        binding.edtCheckOutTime.setText(intent.getStringExtra("check_out_time"));
        binding.edtService.setText(intent.getStringExtra("service"));

        if (intent.getStringExtra("hotel_name") == null) {
            binding.btnManage.setText("Add Hotel");
            getSupportActionBar().setTitle(" Add Hotel");
        } else {
            binding.btnManage.setText("Update Hotel");
            getSupportActionBar().setTitle(" Update Hotel");
        }

//        arrayList = new ArrayList<>();
//
//        binding.rvHotelImage.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
//        binding.rvHotelImage.setItemAnimator(new DefaultItemAnimator());
//
//        adapter = new ImageAdapter(arrayList);
//        binding.rvHotelImage.setAdapter(adapter);

        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);
        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(AddManageHotelActivity.this);
    }

    private void initListener() {

        binding.cardImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        Picasso.get().load(image).placeholder(R.drawable.ic_camera).into(binding.customImageview1);

        binding.edtCheckInTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                // on below line we are getting our hour, minute.
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // on below line we are initializing our Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddManageHotelActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // on below line we are setting selected time
                                // in our text view.
                                binding.edtCheckInTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();
            }
        });

        binding.edtCheckOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                // on below line we are getting our hour, minute.
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // on below line we are initializing our Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddManageHotelActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // on below line we are setting selected time
                                // in our text view.
                                binding.edtCheckOutTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();
            }
        });

        binding.btnManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        if (binding.edtHotelName.getText().toString().equals("")) {
            binding.edtHotelName.setError("Hotel Name is Required");
        } else if (binding.edtCheckInTime.getText().toString().matches("")) {
            binding.edtCheckInTime.setError("CheckIn Time is Required");
        } else if (binding.edtCheckOutTime.getText().toString().equals("")) {
            binding.edtCheckOutTime.setError("CheckOut Time is  Required");
        } else if (binding.edtService.getText().toString().equals("")) {
            binding.edtService.setError("Service is  Required");
        } else if (binding.edtAddress.getText().toString().equals("")) {
            binding.edtAddress.setError("Address Date Required");
        } else if (binding.edtCity.getText().toString().equals("")) {
            binding.edtCity.setError("City is Required");
        } else if (binding.edtLocation.getText().toString().equals("")) {
            binding.edtLocation.setError("Location is  Required");
        } else if (binding.edtState.getText().toString().equals("")) {
            binding.edtState.setError("State is  Required");
        } else if (binding.edtPrice.getText().toString().equals("")) {
            binding.edtPrice.setError("Price is  Required");
        } else if (bitmap == null) {
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            updateData(image);
        } else {
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            uploadImages();
        }
    }

    private void uploadImages() {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] finalImage = byteArrayOutputStream.toByteArray();

        final StorageReference filePath;

        filePath = storageReference.child("Manage Hotel").child(finalImage + "jpg");

        final UploadTask uploadTask = filePath.putBytes(finalImage);

        uploadTask.addOnCompleteListener(AddManageHotelActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(AddManageHotelActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });

    }

    private void updateData(String downloadURL) {
        dbref = reference.child("Manage Hotel");
        String key = reference.push().getKey();

        HashMap params = new HashMap<>();
        params.put("key", key);
        params.put("hotelName", binding.edtHotelName.getText().toString());
        params.put("address", binding.edtAddress.getText().toString());
        params.put("city", binding.edtCity.getText().toString());
        params.put("state", binding.edtState.getText().toString());
        params.put("location", binding.edtLocation.getText().toString());
        params.put("price", binding.edtPrice.getText().toString());
        params.put("checkInTime", binding.edtCheckInTime.getText().toString());
        params.put("checkOutTime", binding.edtCheckOutTime.getText().toString());
        params.put("service", binding.edtService.getText().toString());
        params.put("hotelImage", downloadURL);
        params.put("status", "Active");

//        List<String> hotelImage = new ArrayList<>();
//        hotelImage.add(String.valueOf(arrayList));
//        for(String hotelImages : hotelImage) {
//            dbref.child(key).child(hotelImages).setValue(true);
//        }

        if (toUpdate) {
            dbref.child(uniqueKey).updateChildren(params).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    new CommonMethod(AddManageHotelActivity.this, "Hotel Updated Successfully.");
                    new CommonMethod(AddManageHotelActivity.this, ManageHotelActivity.class);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddManageHotelActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            dbref.child(key).setValue(params).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    progressDialog.dismiss();
                    Toast.makeText(AddManageHotelActivity.this, "Hotel Added Successfully.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddManageHotelActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
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

//    private class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyHolder> {
//        ArrayList<Uri> arrayList;
//
//        public ImageAdapter(ArrayList<Uri> arrayList) {
//            this.arrayList = arrayList;
//        }
//
//        @NonNull
//        @Override
//        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_image, parent, false);
//            return new MyHolder(view);
//        }
//
//        public class MyHolder extends RecyclerView.ViewHolder {
//            ImageView customImage;
//            AppCompatImageView deleteImage;
//
//            public MyHolder(@NonNull View itemView) {
//                super(itemView);
//                customImage = itemView.findViewById(R.id.custom_imageview);
//                deleteImage = itemView.findViewById(R.id.custom_image_delete);
//            }
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
//            holder.customImage.setImageURI(arrayList.get(position));
//
//            holder.deleteImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    arrayList.remove(position);
//                    if (arrayList.isEmpty()) {
//                        binding.ivBannerPlus.setImageResource(R.drawable.ic_add);
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//            });
//
//        }
//
//        public ArrayList<Uri> getData() {
//            return arrayList;
//        }
//
//        @Override
//        public int getItemCount() {
//            return arrayList.size();
//        }
//    }

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