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

import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.Utils.ConnectionDetector;
import com.example.easytravelapplication.databinding.ActivityBuyNowBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class BuyNowActivity extends AppCompatActivity implements PaymentResultListener {

    ActivityBuyNowBinding binding;
    SharedPreferences sp;
    DatabaseReference reference, dbref;
    ProgressDialog progressDialog;
    private String totalDay, totalNight, image;
    String sPaymentType = "", sTransactionId = "";

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
        binding.tvStartingDate.setText(intent.getStringExtra("starting_date"));
        totalDay = intent.getStringExtra("day");
        totalNight = intent.getStringExtra("night");
        binding.tvDayNight.setText(totalDay + "D/" + totalNight + "N");
        binding.tvEndDate.setText(intent.getStringExtra("end_date"));
        binding.tvPrice.setText(intent.getStringExtra("price"));
        image = intent.getStringExtra("image");
        Picasso.get().load(image).into(binding.rivPackage);


        binding.edtName.setText(sp.getString(AppConstant.NAME, ""));
        binding.edtEmail.setText(sp.getString(AppConstant.EMAIL, ""));
        binding.edtContactNo.setText(sp.getString(AppConstant.CONTACT, ""));
        binding.tvDob.setText(sp.getString(AppConstant.DOB, ""));

        if (sp.getString(AppConstant.GENDER, "").equalsIgnoreCase("Female")){
            binding.rgFemale.setChecked(true);
        }
        else{
            binding.rgMale.setChecked(true);

        }
    }

    private void initListener() {
        binding.tvDob.setOnClickListener(new View.OnClickListener() {
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
                } else if (binding.tvDob.getText().toString().equals("")) {
                    binding.tvDob.setError("Date of Birth is  Required");
                } else {
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
        params.put("startingDate", binding.tvStartingDate.getText().toString());
        params.put("endDate", binding.tvEndDate.getText().toString());
        params.put("price", binding.tvPrice.getText().toString());
        params.put("purchaseDate", Calendar.getInstance().getTime().toString());
        params.put("packageImage", image);
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
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
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
                if(new ConnectionDetector(BuyNowActivity.this).isConnectingToInternet()){
                    progressDialog = new ProgressDialog(BuyNowActivity.this);
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    BookPackage();
                }
                else{
                    new ConnectionDetector(BuyNowActivity.this).connectiondetect();
                }
            } else {
                new ConnectionDetector(BuyNowActivity.this).connectiondetect();
            }
        } catch (Exception e) {
            Log.e("RESPONSE", "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Log.d("RESPONSE", "Payment Cancelled " + code + " " + response);
            //Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
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