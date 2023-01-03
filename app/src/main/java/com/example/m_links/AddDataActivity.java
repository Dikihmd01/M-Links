package com.example.m_links;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddDataActivity extends AppCompatActivity {

    Button saveButton, cancelButton, fileButton;
    TextView pageTitle;
    EditText title, description, link;
    DBHelper dbHelper;
//    ImageView imagePreview;
//    String imageUri;

//    ActivityResultLauncher<String> mPickPhoto;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);


//        imagePreview = (ImageView) findViewById(R.id.imagePreview);
        title = (EditText) findViewById(R.id.nama_aplikasi);
        description = (EditText) findViewById(R.id.deskripsi);
        link = (EditText) findViewById(R.id.link);
        saveButton = (Button) findViewById(R.id.btn_save);
        cancelButton = (Button) findViewById(R.id.btn_cancel);
        fileButton = (Button) findViewById(R.id.btn_add_file);
        pageTitle = (TextView) findViewById(R.id.page_title);

        dbHelper = new DBHelper(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (getIntent().getIntExtra("valueId", 0) !=  0) {
            title.setText(getIntent().getStringExtra("valueTitle"));
            description.setText(getIntent().getStringExtra("valueDescription"));
            link.setText(getIntent().getStringExtra("valueLink"));

            pageTitle.setText("Form Update Data");
        }


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getIntExtra("valueId", 0) !=  0) {
                    int id = getIntent().getIntExtra("valueId", 0);
                    Boolean deleteData = dbHelper.deleteData(id);

                    if (deleteData == true) {
                        Toast.makeText(AddDataActivity.this,
                                "Data berhasil dihapus!",
                                Toast.LENGTH_LONG).show();

                        Intent routeMain = new Intent(AddDataActivity.this, MainActivity.class);
                        startActivity(routeMain);
                        AddDataActivity.this.finish();
                    }
                    else {
                        builder.setTitle("Peringatan!")
                                .setMessage("Oops.. Terjadi kesalahan!")
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
                    Intent routeMain = new Intent(AddDataActivity.this, MainActivity.class);
                    startActivity(routeMain);
                    AddDataActivity.this.finish();
                }
            }
        });


//        fileButton.setOnClickListener(view -> {
//            mPickPhoto.launch("image/*");
//        });




//        mPickPhoto = registerForActivityResult(
//                new ActivityResultContracts.GetContent(), result -> {
//                    if(result != null) {
//                        imageUri = result.toString();
//                        imagePreview.setImageURI(result);
//                    }
//                }
//        );


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getTitle = title.getText().toString();
                String getDescription = description.getText().toString();
                String getLink = link.getText().toString();

                if (getTitle.equals("") || getDescription.equals("") || getLink.equals("")) {
                    builder.setTitle("Peringatan!")
                            .setMessage("Field tidak boleh tidak boleh kosong!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).show();
                }
                else {
                    if (getIntent().getIntExtra("valueId", 0) !=  0) {
                        int id = getIntent().getIntExtra("valueId", 0);
                        Boolean updateData = dbHelper.updateDate(getTitle, getDescription, getLink, id);

                        if (updateData == true) {
                            Toast.makeText(AddDataActivity.this,
                                    "Data berhasil diupdate!",
                                    Toast.LENGTH_LONG).show();

                            Intent routeMain = new Intent(AddDataActivity.this, MainActivity.class);
                            startActivity(routeMain);
                            AddDataActivity.this.finish();
                        }
                        else {
                            builder.setTitle("Peringatan!")
                                    .setMessage("Oops.. Terjadi kesalahan!")
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
                        Boolean insertData = dbHelper.insertData(getTitle, getDescription, getLink);
                        if (insertData == true) {
                            Toast.makeText(AddDataActivity.this,
                                    "Data berhasil disimpan!",
                                    Toast.LENGTH_LONG).show();

                            title.setText("");
                            description.setText("");
                            link.setText("");
//                            imageUri = "";

                            Intent routeMain = new Intent(AddDataActivity.this, MainActivity.class);
                            startActivity(routeMain);
                            AddDataActivity.this.finish();
                        }
                        else {
                            builder.setTitle("Peringatan!")
                                    .setMessage("Oops.. Terjadi kesalahan!")
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
            }
        });
    }
}