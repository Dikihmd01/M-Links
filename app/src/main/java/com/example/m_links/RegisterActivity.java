package com.example.m_links;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText username, password, confirmPassword;
    Button registerButton;
    TextView loginButton;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        registerButton = (Button) findViewById(R.id.btn_register);
        loginButton = (TextView) findViewById(R.id.btn_login);
        dbHelper = new DBHelper(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getUsername = username.getText().toString();
                String getPassword = password.getText().toString();
                String getConfirmPassword = confirmPassword.getText().toString();

                if (getUsername.equals("") || getPassword.equals("") || getConfirmPassword.equals("")) {
                    builder.setTitle("Peringatan!")
                            .setMessage("Field tidak boleh kosong!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).show();
                }
                else {
                    if (getUsername.length() >= 8 || getPassword.length() >= 8 || getConfirmPassword.length() >= 8) {
                        if (getUsername.equals(getPassword)) {
                            builder.setTitle("Peringatan!")
                                    .setMessage("Username dan password tidak boleh sama!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    }).show();
                        }
                        else {
                            if (getPassword.equals(getConfirmPassword)) {
                                Boolean checkUser = dbHelper.checkUsername(getUsername);
                                if (checkUser == false) {
                                    Boolean insert = dbHelper.insertAuth(getUsername, getPassword);
                                    if (insert == true) {
                                        Toast.makeText(RegisterActivity.this,
                                                "Selamat, anda telah terdaftar di M-Links!",
                                                Toast.LENGTH_LONG).show();

                                        username.setText("");
                                        password.setText("");
                                        confirmPassword.setText("");

                                        Intent route = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(route);
                                    }
                                    else {
                                        builder.setTitle("Peringatan!")
                                                .setMessage("Oops.. terjadi kesalahan. Silahkan coba lagi!")
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.cancel();
                                                    }
                                                }).show();
                                    }
                                }
                                else {
                                    builder.setTitle("Peringatan!")
                                            .setMessage("Username telah terdaftar, sialhkan login!")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            }).show();
                                }
                            }
                            else {
                                builder.setTitle("Peringatan!")
                                        .setMessage("Oops.. password tidak sama!")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        }).show();
                            }
                        }
                    }
                    else {
                        builder.setTitle("Peringatan!")
                                .setMessage("Username dan password kurang dari 8 karakter!")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                }).show();
                    }
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent routeLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(routeLogin);
                RegisterActivity.this.finish();
            }
        });
    }
}