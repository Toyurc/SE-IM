package com.example.adeba.se_im.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.adeba.se_im.R;
import com.google.firebase.auth.FirebaseAuth;


public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_MS =  2000;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    UserListingActivity.startActivity(SplashActivity.this);
                } else {
                    LoginActivity.startIntent(SplashActivity.this);
                }
                finish();
            }
        };
        mHandler.postDelayed(mRunnable, SPLASH_TIME_MS);

    }
}
