package com.example.m_links;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    int duration = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences prefs = getSharedPreferences("sessionUser", MODE_PRIVATE);
        String displayUsername = prefs.getString("name", "");//"No name defined" is the default value.

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (displayUsername.equalsIgnoreCase("")) {
                    Intent routeLogin = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(routeLogin);
//                    SplashScreen.this.finish();
                }
                else {
                    Intent routeMain = new Intent(SplashScreen.this, HomeActivity.class);
                    startActivity(routeMain);
//                    SplashScreen.this.finish();
                }
                finish();
            }
        }, duration);
    }
}