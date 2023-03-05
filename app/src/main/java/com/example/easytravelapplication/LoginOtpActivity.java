package com.example.easytravelapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.example.easytravelapplication.Model.LogInResponse;
import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.databinding.ActivityLoginOtpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {

    ActivityLoginOtpBinding binding;

    SharedPreferences sp;
    ProgressDialog pd;
    private String verificationId;
    private FirebaseAuth mAuth;
    DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_login_otp);

        mAuth = FirebaseAuth.getInstance();
        //Initialized Child Of Database
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        String phoneNo=getIntent().getStringExtra("CONTACT_NO");
        sendVerificationCode("+91" + phoneNo);
        //sendVerificationCode("+919512464124");

        binding.loginOtp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.loginOtp1.getText().toString().length() == 1) {
                    binding.loginOtp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.loginOtp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.loginOtp2.getText().toString().length() == 1) {
                    binding.loginOtp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.loginOtp2.getText().toString().length() == 1) {
                    binding.loginOtp3.requestFocus();
                } else {
                    binding.loginOtp1.requestFocus();
                }
            }
        });

        binding.loginOtp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.loginOtp3.getText().toString().length() == 1) {
                    binding.loginOtp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.loginOtp3.getText().toString().length() == 1) {
                    binding.loginOtp4.requestFocus();
                } else {
                    binding.loginOtp2.requestFocus();
                }
            }
        });

        binding.loginOtp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.loginOtp4.getText().toString().length() == 1) {
                    binding.loginOtp5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.loginOtp4.getText().toString().length() == 1) {
                    binding.loginOtp5.requestFocus();
                } else {
                    binding.loginOtp3.requestFocus();
                }
            }
        });

        binding.loginOtp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.loginOtp5.getText().toString().length() == 1) {
                    binding.loginOtp6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.loginOtp5.getText().toString().length() == 1) {
                    binding.loginOtp6.requestFocus();
                } else {
                    binding.loginOtp4.requestFocus();
                }
            }
        });

        binding.loginOtp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.loginOtp6.getText().toString().length() == 1) {
                    binding.loginOtp6.setImeOptions(EditorInfo.IME_ACTION_DONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.loginOtp6.getText().toString().length() == 1) {
                    binding.loginOtp6.setImeOptions(EditorInfo.IME_ACTION_DONE);
                } else {
                    binding.loginOtp5.requestFocus();
                }
            }
        });

        binding.loginOtpVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sOTP = binding.loginOtp1.getText().toString() + binding.loginOtp2.getText().toString() + binding.loginOtp3.getText().toString() + binding.loginOtp4.getText().toString() + binding.loginOtp5.getText().toString() + binding.loginOtp6.getText().toString();
                verifyCode(sOTP);
            }
        });

        binding.loginOtpResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode("+91" + sp.getString(AppConstant.CONTACT, ""));
            }
        });

    }

    private void sendVerificationCode(String s) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                s,
                60,
                TimeUnit.SECONDS,
                LoginOtpActivity.this,
                mCallBack
        );
    }

    private void verifyCode(String sOTP) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, sOTP);
        signInWithCredential(credential);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                char[] array = code.toCharArray();
                for (int i = 0; i < array.length; i++) {

                }
//                binding..setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginOtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(LoginOtpActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            if (sp.getString(AppConstant.LOGIN_SIGNUP, "").equalsIgnoreCase("Login")) {
                                sp.edit().putString(AppConstant.IS_LOGIN, "true").commit();
                                new CommonMethod(LoginOtpActivity.this, DashBoardActivity.class);
                            } else {
                                pd = new ProgressDialog(LoginOtpActivity.this);
                                pd.setMessage("Please Wait...");
                                pd.setCancelable(false);
                                pd.show();

                                //Generate UniqueKey For Node In Child
                                final String uniquekey = mdatabase.push().getKey();

                                //UserFirebaseData Class Defined SETTER-GETTER & Constructor
                                LogInResponse userData = new LogInResponse("User",
                                        sp.getString( AppConstant.NAME, "")
                                        , sp.getString( AppConstant.EMAIL, ""),
                                        sp.getString( AppConstant.CONTACT, ""),
                                        sp.getString( AppConstant.GENDER, ""),
                                        sp.getString( AppConstant.DOB, ""),
                                        sp.getString(AppConstant.CITY,""),
                                        sp.getString(AppConstant.STATE,""),
                                        sp.getString(AppConstant.PROFILE_IMAGE, ""),
                                        sp.getString(AppConstant.PASSWORD, "")
                                        );

                                mdatabase.child(uniquekey).setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Dismiss Loading
                                        pd.dismiss();
                                        sp.edit().putString( AppConstant.IS_LOGIN, "true").commit();
                                        new CommonMethod(LoginOtpActivity.this, "Registration Successfully");
                                        new CommonMethod(LoginOtpActivity.this, DashBoardActivity.class);
                                        //onBackPressed();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("RESPONSE", e.getMessage());
                                        pd.dismiss();
                                        new CommonMethod(LoginOtpActivity.this, "Something went wrong");
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(LoginOtpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}