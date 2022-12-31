package com.example.m_links;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView welcomeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeUser = (TextView) findViewById(R.id.nama_user);
        String displayUsername = this.getIntent().getStringExtra("displayUsername");
        welcomeUser.setText(displayUsername + "!");
    }
}