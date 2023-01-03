package com.example.m_links;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView welcomeUser;
    ImageView exitButton;
    FloatingActionButton fab_add;
    Cursor cursor;
    ListAdapter adapter;
    ListView appLists;
    DBHelper dbHelper;
    private int id = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("sessionUser", MODE_PRIVATE);
        String displayUsername = prefs.getString("name", "");//"No name defined" is the default value.

        welcomeUser = (TextView) findViewById(R.id.nama_user);
        exitButton = (ImageView) findViewById(R.id.btn_exit);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        appLists = (ListView) findViewById(R.id.list_aplikasi);
        dbHelper = new DBHelper(this);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        welcomeUser.setText(displayUsername + "!");
        appLists.setSelected(true);
        appLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final CharSequence[] items = {"Edit", "Delete"};
                builder.setTitle("Pilih Operasi")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                                    cursor = db.rawQuery("SELECT * FROM tools", null);
                                    cursor.moveToPosition(i);

                                    Intent routEdit = new Intent(MainActivity.this, AddDataActivity.class);
                                    routEdit.putExtra("valueId", cursor.getInt(0));
                                    routEdit.putExtra("valueTitle", cursor.getString(1));
                                    routEdit.putExtra("valueDescription", cursor.getString(2));
                                    routEdit.putExtra("valueLink", cursor.getString(3));
                                    startActivity(routEdit);
                                    MainActivity.this.finish();
                                }
                                else if (i == 1) {
                                    builder.setTitle("Peringatan!")
                                            .setMessage("Yakin hapus data ini?")
                                            .setCancelable(false)
                                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                                                    cursor = db.rawQuery("SELECT * FROM tools", null);
                                                    if (cursor != null && cursor.moveToFirst()) {
                                                        int id = cursor.getInt(0);
                                                        Boolean deleteData = dbHelper.deleteData(id);
                                                        if (deleteData == true) {
                                                            allData();
                                                            Intent intent = getIntent();
                                                            overridePendingTransition(0, 0);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                            finish();
                                                            overridePendingTransition(0, 0);
                                                            startActivity(intent);
                                                            Toast.makeText(MainActivity.this,
                                                                    "Data berhasil dihapus!",
                                                                    Toast.LENGTH_LONG).show();
                                                        } else {
                                                            builder.setTitle("Peringatan!")
                                                                    .setMessage("Oops.. galagal delete data!")
                                                                    .setCancelable(false)
                                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            dialogInterface.cancel();
                                                                        }
                                                                    }).show();
                                                        }
                                                    }
                                                    else if (cursor != null && cursor.moveToPosition(i)) {
                                                        int id = cursor.getInt(0);
                                                        Boolean deleteData = dbHelper.deleteData(id);
                                                        if (deleteData == true) {
                                                            allData();
                                                            Toast.makeText(MainActivity.this,
                                                                    "Data berhasil dihapus!",
                                                                    Toast.LENGTH_LONG).show();
                                                        } else {
                                                            builder.setTitle("Peringatan!")
                                                                    .setMessage("Oops.. galagal delete data!")
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
                                            })
                                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            }).show();
                                }
                            }
                        }).show();
            }
        });

        allData();

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent routeAddForm = new Intent(MainActivity.this, AddDataActivity.class);
                startActivity(routeAddForm);
                MainActivity.this.finish();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Peringatan!")
                        .setMessage("Yakin mau keluar dari aplikasi?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                prefs.edit().clear().commit();
                                Intent routeLogin = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(routeLogin);
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

    public void allData() {
        cursor = dbHelper.viewData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Tidak ada data untuk ditampilkan!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Ada " + cursor.getCount() + " data!", Toast.LENGTH_LONG).show();
            SQLiteDatabase database = dbHelper.getReadableDatabase();
            try {
                String query = "SELECT * FROM tools";
                cursor = database.rawQuery(query, null);
                adapter = new SimpleCursorAdapter(this, R.layout.list_row, cursor,
                        new String[] {"title","description"},
                        new int[] {R.id.nama_aplikasi, R.id.deskripsi});
                appLists.setAdapter(adapter);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void exit (View view) {

    }
}