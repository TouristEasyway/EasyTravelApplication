package com.example.easytravelapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.databinding.ActivityForgetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class ForgetPasswordActivity extends AppCompatActivity {

    ActivityForgetPasswordBinding binding;
    SharedPreferences sp;

    ProgressDialog pd;

    FirebaseAuth auth;
    DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);

        getSupportActionBar().setTitle("Forget Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        binding.openlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgetPasswordActivity.this, LogInActivity.class));
            }
        });

        binding.forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {
        if (binding.edtEmail.getText().toString().isEmpty()) {
            binding.edtEmail.setError("Email Required");
            binding.edtEmail.requestFocus();
        } else {
            ForgetPassword();
        }
    }

    private void ForgetPassword() {
        pd = new ProgressDialog(ForgetPasswordActivity.this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();

        auth.sendPasswordResetEmail(binding.edtEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    Toast.makeText(ForgetPasswordActivity.this, "check Your Email", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgetPasswordActivity.this, LogInActivity.class));
                    finish();
                } else {
                    pd.dismiss();
                    Toast.makeText(ForgetPasswordActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}