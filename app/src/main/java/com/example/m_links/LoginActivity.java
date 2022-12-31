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

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button loginButton;
    TextView registerButton;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.btn_login);
        registerButton = (TextView) findViewById(R.id.btn_resgister);
        dbHelper = new DBHelper(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getUsername = username.getText().toString();
                String getPassword = password.getText().toString();

                if (getUsername.equals("") || getPassword.equals("")) {
                    // Toast.makeText(getBaseContext(), "Field tidak boleh kosong", Toast.LENGTH_LONG).show();
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
                    Boolean checkUserPass = dbHelper.checkUsernameAndPassword(getUsername, getPassword);
                    if (checkUserPass == true) {
                        Toast.makeText(getBaseContext(), "Anda berhasil login", Toast.LENGTH_LONG).show();

                        username.setText("");
                        password.setText("");

                        Intent route = new Intent(LoginActivity.this, MainActivity.class);
                        route.putExtra("displayUsername", getUsername);
                        startActivity(route);
                    }
                    else {
                        //Toast.makeText(getBaseContext(), "Anda gagal login", Toast.LENGTH_LONG).show();
                        builder.setTitle("Peringatan!")
                                .setMessage("Username dan password tidak cocok!")
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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent routeRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(routeRegister);
                LoginActivity.this.finish();
            }
        });
    }
}