package com.example.m_links;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView welcomeUser, textFilter;
    ImageView exitButton, searchButton, dropDownFilter;
    FloatingActionButton fab_add;
    Cursor cursor;
    EditText keyword;
    ListView appLists;
    DBHelper dbHelper;
    ArrayList<Model> modelArrayList =new ArrayList<>();
    private int id = -1;

    private SharedPreferences prefs;
    private int isAdmin;
    private String displayUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("sessionUser", MODE_PRIVATE);
        displayUsername = prefs.getString("name", "");
        isAdmin = prefs.getInt("is_admin", 0);

        welcomeUser = (TextView) findViewById(R.id.nama_user);
        textFilter = (TextView) findViewById(R.id.textFilter);
        exitButton = (ImageView) findViewById(R.id.btn_exit);
        searchButton = (ImageView) findViewById(R.id.btn_search);
        dropDownFilter = (ImageView) findViewById(R.id.dropDownFilter);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        appLists = (ListView) findViewById(R.id.list_aplikasi);
        keyword = (EditText) findViewById(R.id.search);
        dbHelper = new DBHelper(this);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if (isAdmin == 1) {
            welcomeUser.setText(displayUsername + " (" + "Admin)");

            dropDownFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CharSequence[] items = {"Semua", "Disetujui", "Belum Disetujui"};
                    builder.setTitle("Filter Berdasarkan")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (i == 0) {
                                        textFilter.setText("all");
                                        displayData();
                                    }
                                    else if (i == 1) {
                                        textFilter.setText("accepted");
                                        filterDataAccepted();
                                    }
                                    else {
                                        textFilter.setText("not accepted");
                                        filterDataNotAccepted();
                                    }
                                }
                            }).show();
                }
            });
        }
        else {
            dropDownFilter.setVisibility(View.GONE);
            welcomeUser.setText(displayUsername);
        }
        appLists.setSelected(true);
        displayData();

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent routeAddForm = new Intent(MainActivity.this, AddDataActivity.class);
                startActivity(routeAddForm);
            }
        });

        keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchData();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchData();
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

    public void filterDataAccepted() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM tools WHERE is_accepted = 1";
        Cursor cursor = db.rawQuery(query, null);

        modelArrayList.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            String link = cursor.getString(3);
            byte[] image = cursor.getBlob(4);
            int isAccepted = cursor.getInt(5);

            modelArrayList.add(new Model(id, title, description, link, image, isAccepted));
        }

        Custom adapter = new Custom(this, R.layout.list_row, modelArrayList);
        appLists.setAdapter(adapter);
    }

    public void filterDataNotAccepted() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM tools WHERE is_accepted = 0";
        Cursor cursor = db.rawQuery(query, null);

        modelArrayList.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            String link = cursor.getString(3);
            byte[] image = cursor.getBlob(4);
            int isAccepted = cursor.getInt(5);

            modelArrayList.add(new Model(id, title, description, link, image, isAccepted));
        }

        Custom adapter = new Custom(this, R.layout.list_row, modelArrayList);
        appLists.setAdapter(adapter);
    }

    public void searchData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query;

        if (textFilter.getText().toString() == "all") {
            query = "SELECT * FROM tools WHERE title LIKE '" + keyword.getText().toString() + "%'";
        }
        else if (textFilter.getText().toString() == "accepted") {
            query = "SELECT * FROM tools WHERE title LIKE '" + keyword.getText().toString() + "%' AND + " +
                    "is_accepted = 1";
        }
        else {
            query = "SELECT * FROM tools WHERE title LIKE '" + keyword.getText().toString() + "%' AND + " +
                    "is_accepted = 0";
        }
        Cursor cursor = db.rawQuery(query, null);

        modelArrayList.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            String link = cursor.getString(3);
            byte[] image = cursor.getBlob(4);
            int isAccepted = cursor.getInt(5);

            modelArrayList.add(new Model(id, title, description, link, image, isAccepted));
        }

        Custom adapter = new Custom(this, R.layout.list_row, modelArrayList);
        appLists.setAdapter(adapter);
    }

    public void displayData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query;
        if (isAdmin == 0) {
            query = "SELECT * FROM tools WHERE is_accepted = 1";
        }
        else {
            modelArrayList.clear();
            query = "SELECT * FROM tools";
        }
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            String link = cursor.getString(3);
            byte[] image = cursor.getBlob(4);
            int isAccepted = cursor.getInt(5);
            Log.e("isicursoroooo", "getView: "+ isAccepted );


            modelArrayList.add(new Model(id, title, description, link, image, isAccepted));
        }

        Custom adapter = new Custom(this, R.layout.list_row, modelArrayList);
        appLists.setAdapter(adapter);
    }

    public void exit (View view) {

    }

    private class Custom extends BaseAdapter{
        private Context context;
        private int layout;
        private ArrayList<Model> modelArrayList = new ArrayList<>();

        public Custom(Context context, int layout, ArrayList<Model> modelArrayList) {
            this.context = context;
            this.layout = layout;
            this.modelArrayList = modelArrayList;
        }

        public class ViewHolder {
            TextView title, description, edit, delete, visit, accept;
            ImageView logo;
        }

        @Override
        public int getCount() {
            return modelArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return modelArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            holder.title = view.findViewById(R.id.nama_aplikasi);
            holder.description = view.findViewById(R.id.deskripsi);
            holder.logo = view.findViewById(R.id.logo);
            holder.edit = view.findViewById(R.id.btn_edit);
            holder.delete = view.findViewById(R.id.btn_delete);
            holder.visit = view.findViewById(R.id.btn_visit);
            holder.accept = view.findViewById(R.id.btn_accept);
            view.setTag(holder);

            Model model = modelArrayList.get(i);
            holder.title.setText(model.getTitle());
            holder.description.setText(model.getDescription());
            byte[] image = model.getLogo();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.logo.setImageBitmap(bitmap);

            holder.visit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browser = new Intent(Intent.ACTION_VIEW);
                    browser.setData(Uri.parse(model.getLink()));
                    startActivity(browser);
                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle data = new Bundle();
                    data.putInt("id", model.getId());
                    data.putString("title", model.getTitle());
                    data.putString("description", model.getDescription());
                    data.putString("link", model.getLink());
                    data.putByteArray("logo", model.getLogo());

                    Intent intent = new Intent(MainActivity.this, AddDataActivity.class);
                    intent.putExtra("data", data);
                    startActivity(intent);
                }
            });

            if (isAdmin == 1) {
                holder.accept.setVisibility(View.VISIBLE);
                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        id = model.getId();
                        try {
                            dbHelper.acceptData(id);
                            Toast.makeText(MainActivity.this,
                                    "Aplikasi telah disetujui! ",
                                    Toast.LENGTH_SHORT).show();
                            displayData();

                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this,
                                    "Terjadi kesalahan!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                if (model.getIsAccepted() == 1) {
                    holder.accept.setVisibility(View.GONE);
                }
            }

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    id = model.getId();
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT * FROM tools", null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Peringatan!")
                            .setMessage("Yakin mau keluar dari aplikasi?")
                            .setCancelable(false)
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (cursor != null && cursor.moveToFirst()) {
                                        Boolean deleteData = dbHelper.deleteData(id);
                                        if (deleteData == true) {
                                            displayData();
                                            Intent intent = getIntent();
                                            overridePendingTransition(0, 0);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            finish();
                                            overridePendingTransition(0, 0);
                                            startActivity(intent);
                                            Toast.makeText(MainActivity.this,
                                                    "Data berhasil dihapus!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        else {
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
                                        Boolean deleteData = dbHelper.deleteData(id);
                                        if (deleteData == true) {
                                            displayData();
                                            Toast.makeText(MainActivity.this,
                                                    "Data berhasil dihapus!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        else {
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
            });

            return view;
        }
    }
}