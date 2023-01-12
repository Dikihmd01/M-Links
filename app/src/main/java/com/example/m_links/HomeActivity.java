package com.example.m_links;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private SharedPreferences prefs, prefsMode;
    private SharedPreferences.Editor editor;
    private int isAdmin;
    private String displayUsername;

    private TextView welcomeUser;
    private CardView homeButton, mainButton, guideButton, exitButton;
    private Switch darkAndLightMode;
    private boolean isDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeUser = (TextView) findViewById(R.id.nama_user);
        homeButton = (CardView) findViewById(R.id.btn_home);
        mainButton = (CardView) findViewById(R.id.btn_data);
        guideButton = (CardView) findViewById(R.id.btn_tutor);
        exitButton = (CardView) findViewById(R.id.btn_keluar);

        darkAndLightMode = (Switch) findViewById(R.id.dark_mode);
        prefsMode = getSharedPreferences("mode", Context.MODE_PRIVATE);
        isDarkMode = prefsMode.getBoolean("isDarkMode", false);

        if (isDarkMode) {
            darkAndLightMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        darkAndLightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = prefsMode.edit();
                    editor.putBoolean("isDarkMode", false);
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = prefsMode.edit();
                    editor.putBoolean("isDarkMode", true);
                }
                editor.apply();
            }
        });

        prefs = getSharedPreferences("sessionUser", MODE_PRIVATE);
        displayUsername = prefs.getString("name", "");
        isAdmin = prefs.getInt("is_admin", 0);

        if (isAdmin == 1) {
            welcomeUser.setText(displayUsername + " (" + "Admin)");
        }
        else {
            welcomeUser.setText(displayUsername);
        }

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                startActivity(intent);
            }
        });

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        guideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PanduanActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Konfirmasi!")
                        .setMessage("Yakin mau keluar dari aplikasi?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                prefs.edit().clear().commit();
                                Intent routeLogin = new Intent(HomeActivity.this, LoginActivity.class);
                                startActivity(routeLogin);
                                HomeActivity.this.finish();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();
            }
        });
    }
}