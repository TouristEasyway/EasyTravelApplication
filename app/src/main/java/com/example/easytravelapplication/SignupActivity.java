package com.example.easytravelapplication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    SharedPreferences sp;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    FirebaseDatabase rootNode;
    DatabaseReference reference, dbref;
    ProgressDialog pd;
    FirebaseAuth auth;
    private ArrayList<String> cityList = new ArrayList<>();
    private ArrayList<String> stateList = new ArrayList<>();
    String startDate;

    String city, state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);

        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Initialized Child Of Database
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        cityList.add("Add City");
        cityList.add("Ahmedabad");

        stateList.add("Add State");
        stateList.add("Gujarat");


        ArrayAdapter cityAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.citySpinner.setAdapter(cityAdapter);


        ArrayAdapter stateAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stateList);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.stateSpinner.setAdapter(stateAdapter);


        binding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                city = cityList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                state = stateList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        reference = FirebaseDatabase.getInstance().getReference("Users");

        //Use SharedPreference For Manage Session And Also Save Data Untill We Destroy
        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);

        binding.TIEDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();


                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        SignupActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                binding.TIEDob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                startDate = (dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        },
                        year, month, day);


                datePickerDialog.show();
            }
        });


        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.TIEName.getText().toString().equals("")) {
                    binding.TIEName.setError("Name is Required");
                } else if (binding.TIEEmail.getText().toString().matches("")) {
                    binding.TIEEmail.setError("Email is Required");
                } else if (binding.TIEPhoneNo.getText().toString().equals("")) {
                    binding.TIEPhoneNo.setError("Phone No is  Required");
                } else if (binding.TIEPhoneNo.getText().toString().length() > 10) {
                    binding.TIEDob.setError("10 Digit Phone No Required");
                } else if (binding.radioGroup.getCheckedRadioButtonId() == -1) {
                    new CommonMethod(SignupActivity.this, "Please Select Gender");
                } else if (binding.TIEDob.getText().toString().equals("")) {
                    binding.TIEDob.setError("Date of Birth is  Required");
                } else if (binding.stateSpinner.getSelectedItemPosition() == 0) {
                    new CommonMethod(SignupActivity.this, "Please Select State");
                } else if (binding.citySpinner.getSelectedItemPosition() == 0) {
                    new CommonMethod(SignupActivity.this, "Please Select City");
                } else if (binding.TIEPassword.getText().toString().equals("")) {
                    binding.TIEPassword.setError("Password is  Required");
                } else if (binding.TIEConfirmPassword.getText().toString().equals("")) {
                    binding.TIEConfirmPassword.setError(" Please Enter Confirm is  Required");
                } else {
                    pd = new ProgressDialog(SignupActivity.this);
                    pd.setMessage("Please Wait...");
                    pd.setCancelable(false);
                    pd.show();
//                    Initialized Method For Login
                    createUser();
                }
            }
        });
    }

    private void createUser() {
        auth.createUserWithEmailAndPassword(binding.TIEEmail.getText().toString(), binding.TIEPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    uploadData();
                } else {
                    pd.dismiss();
                    Toast.makeText(SignupActivity.this, "Error Occured : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData() {
        dbref = reference.child("Users");
        String key = reference.push().getKey();

        HashMap<String, String> user = new HashMap<>();
        user.put("key", key);
        user.put("name", binding.TIEName.getText().toString());
        user.put("email", binding.TIEEmail.getText().toString());
        user.put("contact_no", binding.TIEPhoneNo.getText().toString());
        user.put("gender", binding.rgMale.isChecked() ? "Male" : "Female");
        user.put("birth_date", startDate);
        user.put("city", city);
        user.put("state", state);
        user.put("password", binding.TIEPassword.getText().toString());
        user.put("profile_pic", "");
        user.put("user_type", "User");
        user.put("status", "Active");

//        dbref.child(key).child(binding.TIEName.getText().toString()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
        dbref.child(binding.TIEEmail.getText().toString().split("@")[0]).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    sp.edit().putString(AppConstant.KEY, key).commit();
                    pd.dismiss();
                    Toast.makeText(SignupActivity.this, "You are Successfully Registered!", Toast.LENGTH_SHORT).show();
                    openMain();
                } else {
                    pd.dismiss();
                    Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openMain() {
        Intent intent = new Intent(SignupActivity.this, LogInActivity.class);
        startActivity(intent);
        /*Intent intent = new Intent(SignupActivity.this, LoginOtpActivity.class);
        intent.putExtra("CONTACT_NO", binding.TIEPhoneNo.getText().toString());
        startActivity(intent);*/
    }

    @Override
    public void onBackPressed() {
        //Use For Close Application
        //finishAffinity();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}