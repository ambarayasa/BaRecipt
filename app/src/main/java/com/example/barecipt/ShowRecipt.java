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

            //=== sqlite ===
//            final DbHelper dbh = new DbHelper(getApplicationContext());
//            Cursor cursor = dbh.showDetail(id);
//            cursor.moveToFirst();
//            if (cursor.getCount()>0){
//                while(!cursor.isAfterLast()){
//                    tampilNamaResep.setText(cursor.getString(cursor.getColumnIndexOrThrow("nama_resep")));
//                    tampilWaktuMasak.setText(cursor.getString(cursor.getColumnIndexOrThrow("lama_masakan")) + cursor.getString(cursor.getColumnIndexOrThrow("status_lama_masakan")));
//                    tampilPilihanMasakan.setText(cursor.getString(cursor.getColumnIndexOrThrow("pilihan")));
//                    tampilJenisMasakan.setText(cursor.getString(cursor.getColumnIndexOrThrow("jenis")));
//                    tampilBahanMasakan.setText(cursor.getString(cursor.getColumnIndexOrThrow("bahan")));
//                    tampilLangkahMemasak.setText(cursor.getString(cursor.getColumnIndexOrThrow("langkah")));
//                    cursor.moveToNext();
//                }
//            }
        }

//        DbHelper dbHelper = new DbHelper(ShowRecipt.this);
//        SQLiteDatabase db =dbHelper.getReadableDatabase();
//        cursor = db.rawQuery("SELECT * FROM tb_resep WHERE nama_resep = '"+
//                getIntent().getExtras().getString("namaResepIntent")+"'", null);
//        cursor.moveToFirst();
//        if(cursor.getCount()>0){
//            tampilNamaResep.setText(cursor.getString(1).toString());
//            tampilWaktuMasak.setText(cursor.getString(2).toString());
//            tampilPilihanMasakan.setText(cursor.getString(3).toString());
//            tampilJenisMasakan.setText(cursor.getString(4).toString());
//            tampilBahanMasakan.setText(cursor.getString(5).toString());
//            tampilLangkahMemasak.setText(cursor.getString(6).toString());
//        }

//        namaResep = getIntent().getExtras().getString("namaResepIntent");
//        waktuMasak = getIntent().getExtras().getString("waktuMasakIntent");
//        pilihanMasakan = getIntent().getExtras().getString("pilihanMasakanIntent");
//        jenisMasakan = getIntent().getExtras().getString("jenisMasakanIntent");
//        bahanMasakan = getIntent().getExtras().getString("bahanMasakanIntent");
//        langkahMemasak = getIntent().getExtras().getString("langkahMemasakIntent");
//        waktuTipe = getIntent().getExtras().getString("tipeWaktuIntent");
//
//
//        tampilNamaResep.setText(namaResep);
//        tampilWaktuMasak.setText(waktuMasak + " " + waktuTipe);
//        tampilPilihanMasakan.setText(pilihanMasakan);
//        tampilJenisMasakan.setText(jenisMasakan);
//        tampilBahanMasakan.setText(bahanMasakan);
//        tampilLangkahMemasak.setText(langkahMemasak);
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