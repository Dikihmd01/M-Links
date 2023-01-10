package com.example.m_links;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddDataActivity extends AppCompatActivity {

    Button saveButton, cancelButton;
    TextView pageTitle;
    EditText title, description, link;
    DBHelper dbHelper;
    Uri imageUri;
    ImageView logoImg;

    int id = 0;

    private Bitmap bitmap;
    private static final int REQUEST_CODE = 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        logoImg = (ImageView) findViewById(R.id.logo);
        title = (EditText) findViewById(R.id.nama_aplikasi);
        description = (EditText) findViewById(R.id.deskripsi);
        link = (EditText) findViewById(R.id.link);
        saveButton = (Button) findViewById(R.id.btn_save);
        cancelButton = (Button) findViewById(R.id.btn_cancel);
        pageTitle = (TextView) findViewById(R.id.page_title);

        dbHelper = new DBHelper(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        if (getIntent().getIntExtra("valueId", 0) !=  0) {
//            title.setText(getIntent().getStringExtra("valueTitle"));
//            description.setText(getIntent().getStringExtra("valueDescription"));
//            link.setText(getIntent().getStringExtra("valueLink"));
//
//            pageTitle.setText("Form Update Data");
//        }

        updateData();


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent routeMain = new Intent(AddDataActivity.this, MainActivity.class);
                startActivity(routeMain);
                AddDataActivity.this.finish();
            }
        });

        logoImg.setOnClickListener(selectImageListener);


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
//                    updateData();
                    if (getIntent().getBundleExtra("data")!= null) {
                        Boolean updateData = dbHelper.updateDate(getTitle, getDescription, getLink, id, imageViewToBy(logoImg));

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
                        Boolean insertData = dbHelper.insertData(getTitle, getDescription, getLink, imageViewToBy(logoImg));
                        if (insertData == true) {
                            Toast.makeText(AddDataActivity.this,
                                    "Data berhasil disimpan!",
                                    Toast.LENGTH_LONG).show();

                            title.setText("");
                            description.setText("");
                            link.setText("");

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

    private void updateData() {
        if (getIntent().getBundleExtra("data")!= null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            id = bundle.getInt("id");
            title.setText(bundle.getString("title"));
            description.setText(bundle.getString("description"));
            link.setText(bundle.getString("link"));

            byte[] bytes = bundle.getByteArray("logo");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            logoImg.setImageBitmap(bitmap);

            pageTitle.setText("Form Update Data");
        }
    }

    private byte[] imageViewToBy(ImageView logoImg) {
        Bitmap bitmap = ((BitmapDrawable)logoImg.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] bytes = stream.toByteArray();

        return bytes;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(logoImg);
        }
    }

    View.OnClickListener selectImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String SELECT_TYPE = "image/";
            String SELECT_PICTURE = "Select Picture";

            Intent intent = new Intent();
            intent.setType(SELECT_TYPE);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, SELECT_PICTURE), REQUEST_CODE);
        }
    };
}