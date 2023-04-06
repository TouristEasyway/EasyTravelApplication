package com.example.easytravelapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {

    ActivitySplashScreenBinding binding;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    if (sp.getString(AppConstant.IS_LOGIN, "").equalsIgnoreCase("true")) {
                        new CommonMethod(SplashScreenActivity.this, MainActivity.class);
//                        new CommonMethod(SplashScreenActivity.this, DashBoardActivity.class);
                        finish();
                    } else {
                        new CommonMethod(SplashScreenActivity.this, LogInActivity.class);
                        finish();
                    }

            }
        }, 2000);


    }
}