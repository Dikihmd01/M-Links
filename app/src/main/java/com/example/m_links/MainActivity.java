package com.example.m_links;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView welcomeUser;
    ImageView exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeUser = (TextView) findViewById(R.id.nama_user);
        exitButton = (ImageView) findViewById(R.id.btn_exit);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String displayUsername = this.getIntent().getStringExtra("displayUsername");
        welcomeUser.setText(displayUsername + "!");

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Peringatan!")
                        .setMessage("Yakin mau keluar dari aplikasi?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.this.finish();
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

    public void exit (View view) {

    }
}