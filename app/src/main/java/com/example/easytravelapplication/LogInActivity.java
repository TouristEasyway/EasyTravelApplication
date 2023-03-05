package com.example.easytravelapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Model.LogInResponse;
import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.databinding.ActivityLogInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LogInActivity extends AppCompatActivity {

    ActivityLogInBinding binding;
    SharedPreferences sp;

    ProgressDialog pd;

    FirebaseAuth auth;
    DatabaseReference mdatabase;

    ArrayList<LogInResponse> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in);


        //Initialized Firebase
        auth = FirebaseAuth.getInstance();
        //Initialized Child Of Database
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        //Use SharedPreference For Manage Session And Also Save Data Untill We Destroy
        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Used Common Method For Create Global Method Of Intent (Use For Navigate On Next Screen) And Toast(Use For Display Message On Screen)
                new CommonMethod(LogInActivity.this, SignupActivity.class);
            }
        });


        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check Validation For Empty Field Of StudentId And Password
                if (binding.edtUserName.getText().toString().equals("")) {
                    binding.edtUserName.setError("Email Required");
                } else if (binding.edtPassword.getText().toString().equals("")) {
                    binding.edtPassword.setError("Password Required");
                } else {
                    //ProgressDialog Used For Show Loading To User For Showing Data Upload/Fetch Process Will Continue
                    pd = new ProgressDialog(LogInActivity.this);
                    pd.setMessage("Please Wait...");
                    pd.setCancelable(false);
                    pd.show();
                    //Initialized Method For Login
                    doLogin();
//                    loginUser();
                }
            }
        });
    }

    private void loginUser() {
        auth.signInWithEmailAndPassword(binding.edtUserName.getText().toString(), binding.edtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    openMain();
                    pd.dismiss();
                    Log.d("RESPONSE", binding.edtPassword.getText().toString());
                } else {
                    Toast.makeText(LogInActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LogInActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    private void openMain() {
        startActivity(new Intent(LogInActivity.this, DashBoardActivity.class));
        finish();
    }

    private void doLogin() {
        String userEnteredName = binding.edtUserName.getText().toString();
        String userEnteredPassword = binding.edtPassword.getText().toString();
        String childName = binding.edtUserName.getText().toString().split("@")[0];

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        Query checkUser = reference.orderByChild("email").equalTo(userEnteredName);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String passwordDB = dataSnapshot.child(childName).child("password").getValue(String.class);

                    if (passwordDB.equals(userEnteredPassword)) {
                        Toast.makeText(getApplicationContext(), "login Successfully", Toast.LENGTH_LONG).show();

                        sp.edit().putString(AppConstant.IS_LOGIN, "true").commit();
                        sp.edit().putString(AppConstant.LOGIN_SIGNUP, "Login").commit();
                        sp.edit().putString(AppConstant.FIREBASE_CHILD_NAME, childName).commit();
                        sp.edit().putString(AppConstant.USERTYPE, dataSnapshot.child(childName).child("user_type").getValue(String.class)).commit();
                        sp.edit().putString(AppConstant.EMAIL, dataSnapshot.child(childName).child("email").getValue(String.class)).commit();
                        sp.edit().putString(AppConstant.NAME, dataSnapshot.child(childName).child("name").getValue(String.class)).commit();
                        sp.edit().putString(AppConstant.DOB, dataSnapshot.child(childName).child("birth_date").getValue(String.class)).commit();
                        sp.edit().putString(AppConstant.PASSWORD, dataSnapshot.child(childName).child("password").getValue(String.class)).commit();
                        sp.edit().putString(AppConstant.CONTACT, dataSnapshot.child(childName).child("contact_no").getValue(String.class)).commit();
                        sp.edit().putString(AppConstant.CITY, dataSnapshot.child(childName).child("city").getValue(String.class)).commit();
                        sp.edit().putString(AppConstant.STATE, dataSnapshot.child(childName).child("state").getValue(String.class)).commit();
                        sp.edit().putString(AppConstant.GENDER, dataSnapshot.child(childName).child("gender").getValue(String.class)).commit();
                        sp.edit().putString(AppConstant.PROFILE_IMAGE, dataSnapshot.child(childName).child("profile_pic").getValue(String.class)).commit();

                        openMain();

                    } else {
                        new CommonMethod(LogInActivity.this, "Email Not Registered With Our System");
                        pd.dismiss();
                    }
                } else {
                    new CommonMethod(LogInActivity.this, "Invalid Users");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LogInActivity.this, "Error", Toast.LENGTH_LONG).show();

            }
        });

//        mdatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
//                pd.dismiss();
//                arrayList = new ArrayList<>();
//                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
//                    LogInResponse data = snapshot.getValue(LogInResponse.class);
//                    arrayList.add(data);
//                }
//                if (arrayList.size() > 0) {
//                    for (int i = 0; i < arrayList.size(); i++) {
//                        /*if (arrayList.get(i).getStudentId().equals(edtStudentId.getText().toString()) && arrayList.get(i).getPassword().equals(edtpass.getText().toString())) {*/
//                        if (arrayList.get(i).getEmailId() == null) {
//
//                        } else {
//                            if (arrayList.get(i).getEmailId().equalsIgnoreCase(binding.edtUserName.getText().toString()) && arrayList.get(i).getPassword().equalsIgnoreCase(binding.edtPassword.getText().toString())) {
//                                new CommonMethod(LogInActivity.this, "Login Successfully");
//                                //Store Data In SharedPreference For Manage UserType (Admin/Student) And Also LoggedIn Details
//                                sp.edit().putString(AppConstant.IS_LOGIN, "false").commit();
//                                sp.edit().putString(AppConstant.LOGIN_SIGNUP, "Login").commit();
//                                sp.edit().putString(AppConstant.USERTYPE, arrayList.get(i).getUserType()).commit();
//                                sp.edit().putString(AppConstant.EMAIL, arrayList.get(i).getEmailId()).commit();
//                                sp.edit().putString(AppConstant.NAME, arrayList.get(i).getName()).commit();
//                                sp.edit().putString(AppConstant.DOB, arrayList.get(i).getDob()).commit();
//                                sp.edit().putString(AppConstant.EMAIL, arrayList.get(i).getEmailId()).commit();
//                                sp.edit().putString(AppConstant.PASSWORD, arrayList.get(i).getPassword()).commit();
//                                sp.edit().putString(AppConstant.CONTACT, arrayList.get(i).getContact()).commit();
//                                sp.edit().putString(AppConstant.CITY, arrayList.get(i).getCity()).commit();
//                                sp.edit().putString(AppConstant.STATE, arrayList.get(i).getState()).commit();
//                                sp.edit().putString(AppConstant.GENDER, arrayList.get(i).getGender()).commit();
//                                sp.edit().putString(AppConstant.PROFILE_IMAGE, arrayList.get(i).getProfileImage()).commit();
//
//                                new CommonMethod(LogInActivity.this, LoginOtpActivity.class);
//                                break;
//                            } else {
//                                new CommonMethod(LogInActivity.this, "Email Not Registered With Our System");
//                            }
//                        }
//                    }
//                } else {
//                    new CommonMethod(LogInActivity.this, "Invalid Users");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                pd.dismiss();
//                Toast.makeText(LogInActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                arrayList = new ArrayList<>();
//            }
//        });

    }


}