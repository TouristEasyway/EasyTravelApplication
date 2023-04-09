package com.example.easytravelapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Model.ManageCarResponse;
import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.Utils.ConnectionDetector;
import com.example.easytravelapplication.databinding.ActivityBuyCarBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class BuyCarActivity extends AppCompatActivity implements PaymentResultListener {


    ActivityBuyCarBinding binding;
    SharedPreferences sp;
    DatabaseReference reference, dbref;
    ProgressDialog progressDialog;
    ManageCarResponse response;
    String startDate;
    String endDate;
    String sPaymentType = "", sTransactionId = "";

    int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_buy_car);
        response = (ManageCarResponse) getIntent().getSerializableExtra("CAR_RESPONSE");

        getSupportActionBar().setTitle("Book Car");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reference = FirebaseDatabase.getInstance().getReference();

        initView();
        initListener();


    }

    private void initListener() {
        binding.tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();


                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        BuyCarActivity.this,
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
                        BuyCarActivity.this,
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

//        binding.tvBookDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar c = Calendar.getInstance();
//
//
//                int year = c.get(Calendar.YEAR);
//                int month = c.get(Calendar.MONTH);
//                int day = c.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(
//                        // on below line we are passing context.
//                        BuyCarActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//                                // on below line we are setting date to our text view.
//                                binding.tvBookDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//
//                            }
//                        },
//                        year, month, day);
//                datePickerDialog.show();
//
//            }
//        });
        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtName.getText().toString().equals("")) {
                    binding.edtName.setError("Name is Required");
                } else if (binding.edtEmail.getText().toString().matches("")) {
                    binding.edtEmail.setError("Email is Required");
                } else if (binding.edtContactNo.getText().toString().equals("")) {
                    binding.edtContactNo.setError("Phone No is  Required");
                } else if (binding.tvStartDate.getText().equals("")) {
                    binding.tvStartDate.setError("Select Starting Date ");
                } else if (binding.tvEndDate.getText().toString().equals("")) {
                    binding.tvEndDate.setError("Select End Date");
                } else {
                    progressDialog = new ProgressDialog(BuyCarActivity.this);
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
//                    bookCar();
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

        price = Integer.parseInt(dayDifference) * Integer.parseInt(response.getRatePerKM());
        binding.tvPrice.setText(getString(R.string.rupee) + String.valueOf(price));
    }

    private void bookCar() {
        dbref = reference.child("Book Car");
        String key = reference.push().getKey();

        HashMap params = new HashMap<>();
        params.put("key", key);
        params.put("fullName", binding.edtName.getText().toString());
        params.put("contactNo", binding.edtContactNo.getText().toString());
        params.put("email", binding.edtEmail.getText().toString());
        params.put("startDate", binding.tvStartDate.getText().toString());
        params.put("endDate", binding.tvEndDate.getText().toString());
        params.put("carName", response.getCarName());
        params.put("fuelType", response.getfuelType());
        params.put("carType", response.getCarType());
        params.put("bookDate", new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()));
        params.put("price", binding.tvPrice.getText().toString());
        params.put("ratePerKM", response.getRatePerKM());
        params.put("available", response.getAvailable());
        params.put("city", response.getCity());
        params.put("transactionId", sTransactionId);
        params.put("paymentType", sPaymentType);
        params.put("status", "Active");

        dbref.child(key).setValue(params).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                progressDialog.dismiss();
                new CommonMethod(BuyCarActivity.this, "Car Booked Successfully.");
                new CommonMethod(BuyCarActivity.this, MainActivity.class);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(BuyCarActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void initView() {
        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);
        binding.tvCarName.setText(response.getCarName());
        binding.tvCarType.setText(response.getCarType());
        binding.tvFuelType.setText(response.getfuelType());
        binding.tvRate.setText(response.getRatePerKM() + "/Day");
        binding.tvAvailble.setText(response.getAvailable());
        binding.tvCity.setText(response.getCity());
        binding.edtName.setText(sp.getString(AppConstant.NAME, ""));
        binding.edtEmail.setText(sp.getString(AppConstant.EMAIL, ""));
        binding.edtContactNo.setText(sp.getString(AppConstant.CONTACT, ""));
    }


    private void launchRazorpayPayment() {
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            //options.put("description", "Demoing Charges");
            options.put("description", "Book Car Details");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", price * 100);

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
            if (new ConnectionDetector(BuyCarActivity.this).isConnectingToInternet()) {
                if (new ConnectionDetector(BuyCarActivity.this).isConnectingToInternet()) {
                    progressDialog = new ProgressDialog(BuyCarActivity.this);
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    bookCar();
                } else {
                    new ConnectionDetector(BuyCarActivity.this).connectiondetect();
                }
            } else {
                new ConnectionDetector(BuyCarActivity.this).connectiondetect();
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