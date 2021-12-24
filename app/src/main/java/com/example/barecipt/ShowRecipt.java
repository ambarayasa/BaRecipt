package com.example.barecipt;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowRecipt extends AppCompatActivity {
    private TextView tampilNamaResep, tampilWaktuMasak, tampilPilihanMasakan, tampilJenisMasakan,
            tampilBahanMasakan, tampilLangkahMemasak, tampilStatusWaktuMasak;
    private String namaResep, lamaMemasak, statusLamaMemasak, pilihan, jenis, bahan, langkah;
    private Integer id = 0;
    private ArrayList<ReciptHandler> resepHandler = new ArrayList<ReciptHandler>();
    private DbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipt_show);

        tampilNamaResep = findViewById(R.id.namaMasakan);
        tampilWaktuMasak = findViewById(R.id.inputLamaMemasak);
        tampilStatusWaktuMasak = findViewById(R.id.inputStatusLamaMemasak);
        tampilPilihanMasakan = findViewById(R.id.pilihanMasakan);
        tampilJenisMasakan = findViewById(R.id.jenisMasakan);
        tampilBahanMasakan = findViewById(R.id.bahanMasakan);
        tampilLangkahMemasak = findViewById(R.id.langkahMemasak);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        namaResep = intent.getStringExtra("nama_resep");
        lamaMemasak = intent.getStringExtra("lama_masakan");
        statusLamaMemasak = intent.getStringExtra("status_lama_masakan");
        pilihan = intent.getStringExtra("pilihan");
        jenis = intent.getStringExtra("jenis");
        bahan = intent.getStringExtra("bahan");
        langkah = intent.getStringExtra("langkah");
//        Toast.makeText(ShowRecipt.this,String.valueOf(id),Toast.LENGTH_SHORT).show();

        if(id>0){
            tampilNamaResep.setText(String.valueOf(namaResep));
            tampilWaktuMasak.setText(String.valueOf(lamaMemasak));
            tampilStatusWaktuMasak.setText(String.valueOf(statusLamaMemasak));
            tampilPilihanMasakan.setText(String.valueOf(pilihan));
            tampilJenisMasakan.setText(String.valueOf(jenis));
            tampilBahanMasakan.setText(String.valueOf(bahan));
            tampilLangkahMemasak.setText(String.valueOf(langkah));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "Menampilkan Activity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this,"Menjeda Activity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Toast.makeText(this," Memulai Activity Kembali", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Menghancurkan Activity", Toast.LENGTH_SHORT).show();
    }
}