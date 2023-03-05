package com.example.easytravelapplication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.databinding.ActivityBookHotelBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

public class BookHotelActivity extends AppCompatActivity {

    ActivityBookHotelBinding binding;
    SharedPreferences sp;
    DatabaseReference reference, dbref;
    ProgressDialog progressDialog;
    private String address,city, state, location, checkInDate, checkOutDate, dayDifference, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_hotel);

        reference = FirebaseDatabase.getInstance().getReference();
        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);

        initView();
        initListener();
    }

    private void initView() {
        getSupportActionBar().setTitle(" Booking Page");

//        try {
////            String CurrentDate= "09/24/2018";
////            String FinalDate= "09/26/2019";
//            Date date1;
//            Date date2;
//            SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yyyy");
//            date1 = dates.parse(checkInDate);
//            date2 = dates.parse(checkOutDate);
//            long difference = Math.abs(date1.getTime() - date2.getTime());
//            long differenceDates = difference / (24 * 60 * 60 * 1000);
//            dayDifference = Long.toString(differenceDates);
//        } catch (Exception exception) {
//            Toast.makeText(this, "Unable to find difference", Toast.LENGTH_SHORT).show();
//        }

        Intent intent = getIntent();
        binding.tvHotelName.setText(intent.getStringExtra("hotel_name"));
        binding.tvPlaces.setText(intent.getStringExtra("address") + ", " + intent.getStringExtra("city"));
        binding.tvCheckInTime.setText(intent.getStringExtra("check_in_time"));
        binding.tvCheckOutTime.setText(intent.getStringExtra("check_in_time"));
        address = intent.getStringExtra("address");
        city = intent.getStringExtra("city");
        state = intent.getStringExtra("state");
        location = intent.getStringExtra("location");
        binding.tvPrice.setText(intent.getStringExtra("price") + " per Night");
//        int price = Integer.parseInt(intent.getStringExtra("price").replace(",",""));
//        int totalDay = Integer.parseInt(dayDifference);
        binding.tvTotalAmount.setText(intent.getStringExtra("price"));

        image = intent.getStringExtra("hotel_image");
        Picasso.get().load(image).placeholder(R.drawable.ic_person).into(binding.rivHotel);
    }

    private void initListener() {
        binding.edtCheckInDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        BookHotelActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                binding.edtCheckInDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                checkInDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        binding.edtCheckOutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();


                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        BookHotelActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                binding.edtCheckOutDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                checkOutDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.edtName.getText().toString().equals("")) {
                    binding.edtName.setError("Name is Required");
                } else if (binding.edtEmail.getText().toString().matches("")) {
                    binding.edtEmail.setError("Email is Required");
                } else if (binding.edtContactNo.getText().toString().equals("")) {
                    binding.edtContactNo.setError("Phone No is  Required");
                } else if (binding.edtCheckInDate.getText().toString().equals("")) {
                    binding.edtCheckInDate.setError("CheckIn Date is  Required");
                } else if (binding.edtCheckOutDate.getText().toString().equals("")) {
                    binding.edtCheckOutDate.setError("CheckOut Date is  Required");
                } else {
                    progressDialog = new ProgressDialog(BookHotelActivity.this);
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    BookHotel();
//                    Initialized Method For Login
//                    launchRazorpayPayment();
                }
            }
        });
    }

    private void BookHotel() {
        dbref = reference.child("Book Hotel");
        String key = reference.push().getKey();

        HashMap params = new HashMap<>();
        params.put("key", key);
        params.put("userName", binding.edtName.getText().toString());
        params.put("userContact", binding.edtContactNo.getText().toString());
        params.put("userEmail", binding.edtEmail.getText().toString());
        params.put("address", address);
        params.put("city", city);
        params.put("state", state);
        params.put("location", location);
        params.put("checkInDate", binding.edtCheckInDate.getText().toString());
        params.put("checkOutDate", binding.edtCheckOutDate.getText().toString());
        params.put("totalPerson", binding.edtTotalPerson.getText().toString());
        params.put("hotelName", binding.tvHotelName.getText().toString());
        params.put("price", binding.tvPrice.getText().toString());
        params.put("totalAmount", binding.tvTotalAmount.getText().toString());
        params.put("bookingDate", Calendar.getInstance().getTime().toString());
        params.put("service", binding.tvPrice.getText().toString());
        params.put("checkInTime", binding.tvCheckInTime.getText().toString());
        params.put("checkOutTime", binding.tvCheckInTime.getText().toString());
        params.put("hotelImage", image);
        params.put("status", "Active");

        dbref.child(key).setValue(params).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                progressDialog.dismiss();
                Toast.makeText(BookHotelActivity.this, "Hotel Booked Successfully.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(BookHotelActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //    private void launchRazorpayPayment() {
//        final Activity activity = this;
//
//        final Checkout co = new Checkout();
//
//        try {
//            JSONObject options = new JSONObject();
//            options.put("name", "Razorpay Corp");
//            //options.put("description", "Demoing Charges");
//            options.put("description", "Product Details");
//            options.put("send_sms_hash", true);
//            options.put("allow_rotation", true);
//            //You can omit the image option to fetch the image from dashboard
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("currency", "INR");
//            options.put("amount", Double.parseDouble(binding.tvPrice.getText().toString()) * 100);
//
//            JSONObject preFill = new JSONObject();
//            preFill.put("email", sp.getString(AppConstant.EMAIL, ""));
//            preFill.put("contact", sp.getString(AppConstant.CONTACT, ""));
//
//            options.put("prefill", preFill);
//
//            co.open(activity, options);
//        } catch (Exception e) {
//            Log.d("RESPONSE", e.getMessage());
//            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
//                    .show();
//            e.printStackTrace();
//        }
//    }
}