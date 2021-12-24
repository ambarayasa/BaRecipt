package com.example.barecipt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditRecipt extends AppCompatActivity {
    private TextView namaResep, bahanMasakan, langkahMemasak, waktu, status, inputMasakanLain;
    private SeekBar seekbarWaktuMasak;
    private String resep_nama, hasilWaktuMasak, resep_pilihan, resep_bahan, resep_langkah, waktu_sementara, resep_status_waktu, resep_waktu;
    private String hasilJenisMasakan = "";
    private RadioGroup radioGroup_pilihanMasakan, radiogroupWaktuMasak;
    private RadioButton radioButtonPilihanMasakan, vegetarian, nonVegetarian, status_menit, status_jam, status_hari;
    private Button btnSubmit;
    private CheckBox masakanBali, masakanIndonesia, masakanEropa, masakanChina, masakanLain;
    private Button btnHapus;
    private String namaResepMemasak, lamaMemasak, statusLamaMemasak, pilihan, jenis, bahan, langkah;
    private SharedPreferences preferences;
    private ArrayList<ReciptHandler> resepHandler = new ArrayList<ReciptHandler>();
    private int position = 0;
    private int id = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        namaResep = findViewById(R.id.inputNamaResep);
        waktu = findViewById(R.id.nilaiWaktuMasak);
        status = findViewById(R.id.statusWaktuMasak);
        seekbarWaktuMasak = findViewById(R.id.seekbarWaktuMasak);
        radioGroup_pilihanMasakan = findViewById(R.id.radioGroup_pilihanMasakan);
        radiogroupWaktuMasak = findViewById(R.id.radiogroup_waktuMasak);
        masakanBali = findViewById(R.id.masakanBali);
        masakanIndonesia = findViewById(R.id.masakanIndonesia);
        masakanEropa = findViewById(R.id.masakanEropa);
        masakanChina = findViewById(R.id.masakanChina);
        masakanLain = findViewById(R.id.masakanLain);
        inputMasakanLain = findViewById(R.id.inputMasakanLain);
        bahanMasakan = findViewById(R.id.inputBahanMasakan);
        langkahMemasak = findViewById(R.id.inputLangkahMemasak);
        vegetarian = findViewById(R.id.veget);
        nonVegetarian = findViewById(R.id.nonVeget);
        status_menit = findViewById(R.id.radio_menit);
        status_jam = findViewById(R.id.radio_jam);
        status_hari = findViewById(R.id.radio_hari);
        btnSubmit = findViewById(R.id.btn_submit);
        btnHapus = findViewById(R.id.btn_delete);
        String result = "";

        position = getIntent().getIntExtra("position", 0);
        id = getIntent().getIntExtra("id", 0);
        namaResepMemasak = getIntent().getStringExtra("nama_resep");
        lamaMemasak = getIntent().getStringExtra("lama_masakan");
        statusLamaMemasak = getIntent().getStringExtra("status_lama_masakan");
        pilihan = getIntent().getStringExtra("pilihan");
        jenis = getIntent().getStringExtra("jenis");
        bahan = getIntent().getStringExtra("bahan");
        langkah = getIntent().getStringExtra("langkah");

        if (id > 0) {
//            final DbHelper dbh = new DbHelper(getApplicationContext());
//            Cursor cursor = dbh.showDetail(id);
//            cursor.moveToFirst();
//            if (cursor.getCount()>0){
//                while(!cursor.isAfterLast()){
            namaResep.setText(String.valueOf(namaResepMemasak));
            waktu.setText(String.valueOf(lamaMemasak));
            status.setText(String.valueOf(statusLamaMemasak));
            if (String.valueOf(statusLamaMemasak).equals(" menit")) {
                status_menit.setChecked(true);
            } else if (String.valueOf(statusLamaMemasak).equals(" jam")) {
                status_jam.setChecked(true);
            } else if (String.valueOf(statusLamaMemasak).equals(" hari")) {
                status_hari.setChecked(true);
            }
            if (String.valueOf(pilihan).equals("Vegetarian")) {
                vegetarian.setChecked(true);
            } else if (String.valueOf(pilihan).equals("Non Vegetarian")) {
                nonVegetarian.setChecked(true);
            }
            result = (String.valueOf(jenis));

            if (result.contains("Masakan Bali")) {
                masakanBali.setChecked(true);
            }
            if (result.contains("Masakan Indonesia")) {
                masakanIndonesia.setChecked(true);
            }
            if (result.contains("Masakan Eropa")) {
                masakanEropa.setChecked(true);
            }
            if (result.contains("Masakan China")) {
                masakanChina.setChecked(true);
            }
            if (!result.contains("Masakan Bali") && !result.contains("Masakan Indonesia") && !result.contains("Masakan Eropa") && !result.contains("Masakan China")) {
                masakanLain.setChecked(true);
            }

            bahanMasakan.setText(String.valueOf(bahan));
            langkahMemasak.setText(String.valueOf(langkah));
//                    cursor.moveToNext();
//                }
//            }
        }
        //seekbar
        waktu_sementara = waktu.getText().toString();
        seekbarWaktuMasak.setProgress(Integer.parseInt(waktu_sementara));
        seekbarWaktuMasak.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                waktu_sementara = String.valueOf(i);
                waktu.setText(waktu_sementara);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        radiogroupWaktuMasak.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_menit:
                        status.setText(" menit");
                        break;
                    case R.id.radio_jam:
                        status.setText(" jam");
                        break;
                    case R.id.radio_hari:
                        status.setText(" hari");
                        break;
                }
            }
        });

        masakanLain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    inputMasakanLain.setVisibility(View.VISIBLE);
                }
                if (!b) {
                    inputMasakanLain.setVisibility(View.GONE);
                }
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogAlertBuilder = new AlertDialog.Builder(EditRecipt.this);
                dialogAlertBuilder.setTitle("Konfirmasi");
                dialogAlertBuilder
                        .setMessage("Yakin menghapus data?")
                        .setPositiveButton("Yakin", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                DbHelper dbh = new DbHelper(getApplicationContext());
//
//                                boolean hapusData = dbh.deleteData(id);
//
//                                if (hapusData) {
//                                    Toast.makeText(EditRecipt.this, "Hapus Peminjaman Berhasil", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(EditRecipt.this, "Hapus Peminjaman Gagal", Toast.LENGTH_SHORT).show();
//                                }

                                deleteResepWebserver(id, position);
//                                Intent mainIntent = new Intent(EditRecipt.this, MainActivity.class);
//                                startActivity(mainIntent);
                                finish();
                            }
                        })
                        .setNegativeButton(Html.fromHtml("<font color='#48494B'>Tidak</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog dialog = dialogAlertBuilder.create();
                dialog.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //radiobutton
                hasilJenisMasakan = "";

                if (masakanBali.isChecked()) {
                    hasilJenisMasakan += masakanBali.getText().toString() + "\n";
                }
                if (masakanIndonesia.isChecked()) {
                    hasilJenisMasakan += masakanIndonesia.getText().toString() + "\n";
                }
                if (masakanEropa.isChecked()) {
                    hasilJenisMasakan += masakanEropa.getText().toString() + "\n";
                }
                if (masakanChina.isChecked()) {
                    hasilJenisMasakan += masakanChina.getText().toString() + "\n";
                }
                if (masakanLain.isChecked()) {
                    hasilJenisMasakan += inputMasakanLain.getText().toString() + "\n";
                }

                int radioId = radioGroup_pilihanMasakan.getCheckedRadioButtonId();
                radioButtonPilihanMasakan = findViewById(radioId);

                resep_nama = namaResep.getText().toString();
                resep_pilihan = radioButtonPilihanMasakan.getText().toString();
                resep_bahan = bahanMasakan.getText().toString();
                resep_langkah = langkahMemasak.getText().toString();
                resep_status_waktu = status.getText().toString();
                resep_waktu = waktu.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(EditRecipt.this);
                builder.setIcon(R.drawable.warning_round);
                builder.setTitle("Edit Resep");
                builder.setMessage(
                        "Apakah kamu sudah selesai dengan resep ini ?\n\n" +
                                "Nama Masakan  : " + resep_nama + "\n\n" +
                                "Lama Memasak  : " + resep_waktu + resep_status_waktu + "\n\n" +
                                "Pilihan Masakan  : " + resep_pilihan + "\n\n" +
                                "Jenis Masakan  : " + hasilJenisMasakan + "\n" +
                                "Bahan Masakan  : " + resep_bahan + "\n\n" +
                                "Langkah Memasak  : " + resep_langkah + ""
                );
                builder.setPositiveButton("Selesai", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Masakan berhasil kamu simpan !", Toast.LENGTH_SHORT).show();
                        editResepWebserver();
                        //    === sqlite ===
//                            DbHelper db = new DbHelper(getApplicationContext());
//                            ReciptHandler resepHandler = new ReciptHandler();
//                            resepHandler.setNamaResep(resep_nama);
//                            resepHandler.setLamaMemasak(waktu_sementara);
//                            resepHandler.setStatusLamaMemasak(resep_status_waktu);
//                            resepHandler.setPilihan(resep_pilihan);
//                            resepHandler.setJenis(hasilJenisMasakan);
//                            resepHandler.setBahan(resep_bahan);
//                            resepHandler.setLangkah(resep_langkah);

//                            boolean editResep = db.editData(resepHandler, id);
//
//                            if(editResep){
//                                Toast.makeText(EditRecipt.this, "Berhasil Edit Data", Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(EditRecipt.this, "Gagal Edit Data", Toast.LENGTH_SHORT).show();
//                            }
//                            db.close();

//                        Intent intent = new Intent(EditRecipt.this, MainActivity.class);
//                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton(Html.fromHtml("<font color='#48494B'>Belum</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void editResepWebserver() {
        String url = Constant.UPDATE_RESEP;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                ReciptHandler resepHandlerList = MainActivity.resepHandler.get(position);
                                resepHandlerList.setNamaResep(resep_nama);
                                resepHandlerList.setLamaMemasak(waktu_sementara);
                                resepHandlerList.setStatusLamaMemasak(resep_status_waktu);
                                resepHandlerList.setPilihan(resep_pilihan);
                                resepHandlerList.setJenis(hasilJenisMasakan);
                                resepHandlerList.setBahan(resep_bahan);
                                resepHandlerList.setLangkah(resep_langkah);
                                MainActivity.resepHandler.set(position, resepHandlerList);
                                MainActivity.recyclerView.getAdapter().notifyItemChanged(position);
                                MainActivity.recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> {
            error.printStackTrace();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", id + "");
                map.put("nama_resep", resep_nama);
                map.put("lama_masakan", waktu_sementara);
                map.put("status_lama_masakan", resep_status_waktu);
                map.put("pilihan", resep_pilihan);
                map.put("jenis", hasilJenisMasakan);
                map.put("bahan", resep_bahan);
                map.put("langkah", resep_langkah);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void deleteResepWebserver(int id, int position) {
        String url = Constant.DELETE_RESEP;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")) {
                                ReciptHandler resepHandlerList = MainActivity.resepHandler.get(position);
                                MainActivity.resepHandler.set(position, resepHandlerList);
                                MainActivity.recyclerView.getAdapter().notifyItemChanged(position);
                                MainActivity.recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> {
            error.printStackTrace();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", id + "");
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
