package com.example.easytravelapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Model.HotelListResponse;
import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.Utils.ConnectionDetector;
import com.example.easytravelapplication.databinding.ActivityBuyNowBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class BuyNowActivity extends AppCompatActivity implements PaymentResultListener {

    ActivityBuyNowBinding binding;
    SharedPreferences sp;
    DatabaseReference reference, dbref;
    ProgressDialog progressDialog;
    private String totalDay, totalNight, image;
    String sPaymentType = "", sTransactionId = "";
    String startDate;
    String endDate;
    String hotelName;
    int totalAmount;
    ArrayList<HotelListResponse> hotelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_buy_now);

        getSupportActionBar().setTitle("Book Package");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reference = FirebaseDatabase.getInstance().getReference();
        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);

        initView();
        initListener();
    }

    private void initView() {

        Intent intent = getIntent();
        binding.tvPackageName.setText(intent.getStringExtra("package_name"));
        binding.tvPlaces.setText(intent.getStringExtra("places"));
        totalDay = intent.getStringExtra("day");
        totalNight = intent.getStringExtra("night");
        binding.tvDayNight.setText(totalDay + "D/" + totalNight + "N");
        binding.tvEndDate.setText(intent.getStringExtra("end_date"));
        binding.tvPrice.setText(intent.getStringExtra("price"));
        binding.tvHotelName.setText(intent.getStringExtra("hotel_name"));
        image = intent.getStringExtra("image");
        Picasso.get().load(image).into(binding.rivPackage);

        binding.edtName.setText(sp.getString(AppConstant.NAME, ""));
        binding.edtEmail.setText(sp.getString(AppConstant.EMAIL, ""));
        binding.edtContactNo.setText(sp.getString(AppConstant.CONTACT, ""));

        if (sp.getString(AppConstant.GENDER, "").equalsIgnoreCase("Female")) {
            binding.rgFemale.setChecked(true);
        } else {
            binding.rgMale.setChecked(true);
        }

//        getHotelName();

//        HotelListResponse response = new HotelListResponse();
//        response.setHotelName("Add Hotels");
//        hotelList.add(0,response);
//        ArrayAdapter hotelAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,hotelList);
//        hotelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        binding.hotels.setAdapter(hotelAdapter);
//
//        binding.hotels.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                hotelName = hotelList.get(i).getHotelName();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }

    private void getHotelName() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Manage Hotel");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HotelListResponse data = snapshot.getValue(HotelListResponse.class);
                        hotelList.add(data);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void initListener() {

        /*binding.tvDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();


                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        BuyNowActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                binding.tvDob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });*/

        binding.tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();


                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        BuyNowActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                binding.tvStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                startDate = (dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        },
                        year, month, day);


                datePickerDialog.show();
            }
        });

        binding.tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();


                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        BuyNowActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                binding.tvEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                endDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

                                countDays();
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
                } else if (binding.radioGroup.getCheckedRadioButtonId() == -1) {
                    new CommonMethod(BuyNowActivity.this, "Please Select Gender");
                }
                /*else if (binding.tvDob.getText().toString().equals("")) {
                    binding.tvDob.setError("Date of Birth is  Required");
                }*/
                else {
                    progressDialog = new ProgressDialog(BuyNowActivity.this);
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
//                    BookPackage();
//                    Initialized Method For Login
                    launchRazorpayPayment();
                }
            }
        });
    }


    private void countDays() {
        Date date1;
        Date date2;
        SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date1 = dates.parse(startDate);
            date2 = dates.parse(endDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long difference = Math.abs(date1.getTime() - date2.getTime());
        long differenceDates = difference / (24 * 60 * 60 * 1000);
        String dayDifference = Long.toString(differenceDates);
        Log.d("TAG", dayDifference);

        totalAmount = Integer.parseInt(dayDifference) * Integer.parseInt(getIntent().getStringExtra("price"));
        binding.tvPrice.setText(String.valueOf(totalAmount));
    }

    private void BookPackage() {
        dbref = reference.child("Book Packages");
        String key = reference.push().getKey();

        HashMap params = new HashMap<>();
        params.put("key", key);
        params.put("userName", binding.edtName.getText().toString());
        params.put("userContact", binding.edtContactNo.getText().toString());
        params.put("userEmail", binding.edtEmail.getText().toString());
        params.put("packageName", binding.tvPackageName.getText().toString());
        params.put("places", binding.tvPlaces.getText().toString());
        params.put("totalDay", totalDay);
        params.put("totalNight", totalNight);
        params.put("hotelName", hotelName);
        params.put("startingDate", binding.tvStartDate.getText().toString());
        params.put("endDate", binding.tvEndDate.getText().toString());
        params.put("price", binding.tvPrice.getText().toString());
        params.put("purchaseDate", Calendar.getInstance().getTime().toString());
        params.put("packageImage", image);
        params.put("transactionId", sTransactionId);
        params.put("paymentType", sPaymentType);
        params.put("status", "Active");

        dbref.child(key).setValue(params).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                progressDialog.dismiss();
                Toast.makeText(BuyNowActivity.this, "Package Booked Successfully.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(BuyNowActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void launchRazorpayPayment() {
        final Activity activity = this;

        final Checkout co = new Checkout();
        //demo123@yopmail.com
        //demo@123
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            //options.put("description", "Demoing Charges");
            options.put("description", "Buy Now");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", Double.parseDouble(binding.tvPrice.getText().toString()) * 100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", sp.getString(AppConstant.EMAIL, ""));
            preFill.put("contact", sp.getString(AppConstant.CONTACT, ""));

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Log.d("RESPONSE", e.getMessage());
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            //Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            sTransactionId = razorpayPaymentID;
            sPaymentType = "Online";
            if (new ConnectionDetector(BuyNowActivity.this).isConnectingToInternet()) {
                if (new ConnectionDetector(BuyNowActivity.this).isConnectingToInternet()) {

                    progressDialog.show();
                    BookPackage();
                    progressDialog.dismiss();

                } else {
                    new ConnectionDetector(BuyNowActivity.this).connectiondetect();
                    progressDialog.dismiss();
                }
            } else {
                new ConnectionDetector(BuyNowActivity.this).connectiondetect();
                progressDialog.dismiss();

            }
        } catch (Exception e) {
            Log.e("RESPONSE", "Exception in onPaymentSuccess", e);
            progressDialog.dismiss();

        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Log.d("RESPONSE", "Payment Cancelled " + code + " " + response);
            //Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } catch (Exception e) {
            Log.e("RESPONSE", "Exception in onPaymentError", e);
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