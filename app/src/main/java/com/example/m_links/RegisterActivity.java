package com.example.m_links;

import androidx.appcompat.app.AppCompatActivity;

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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getUsername = username.getText().toString();
                String getPassword = password.getText().toString();
                String getConfirmPassword = confirmPassword.getText().toString();

                if (getUsername.equals("") || getPassword.equals("") || getConfirmPassword.equals("")) {
                    Toast.makeText(getBaseContext(), "Field tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
                else {
                    if (getPassword.equals(getConfirmPassword)) {
                        Boolean checkUser = dbHelper.checkUsername(getUsername);
                        if (checkUser == false) {
                            Boolean insert = dbHelper.insertAuth(getUsername, getPassword);
                            if (insert == true) {
                                Toast.makeText(RegisterActivity.this, "Selamat, anda telah terdaftar di M-Links!", Toast.LENGTH_LONG).show();

                                Intent route = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(route);
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "Oops.. terjadi kesalahan. Silahkan coba lagi!", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Username telah terdaftar, sialhkan logiin!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Oops.. password tidak sama!", Toast.LENGTH_LONG).show();
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