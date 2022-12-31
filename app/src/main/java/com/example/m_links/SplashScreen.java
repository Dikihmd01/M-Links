package com.example.m_links;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    int duration = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent routeLogin = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(routeLogin);
                SplashScreen.this.finish();
            }
        }, duration);
    }
}