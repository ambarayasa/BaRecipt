package com.example.barecipt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateRecipt extends AppCompatActivity {
    private TextView namaResep, bahanMasakan, langkahMemasak, waktu, status, inputMasakanLain;
    private SeekBar seekbarWaktuMasak;
    private String resep_nama, hasilWaktuMasak, resep_pilihan, resep_bahan, resep_langkah, resep_status_waktu, resep_waktu;
    private String hasilJenisMasakan = "";
    private RadioGroup radioGroup_pilihanMasakan, radiogroupWaktuMasak;
    private RadioButton radioButtonPilihanMasakan;
    private Button btnSubmit;
    private CheckBox masakanBali, masakanIndonesia, masakanEropa, masakanChina, masakanLain;
    private boolean isValidasi = false;
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipt_input);

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
        btnSubmit = findViewById(R.id.btn_submit);
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        //seekbar
        seekbarWaktuMasak.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                hasilWaktuMasak = String.valueOf(i);
                waktu.setText(hasilWaktuMasak);
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
                switch (checkedId){
                    case R.id.radio_menit:
                        status.setText(" menit" );
                        break;
                    case R.id.radio_jam:
                        status.setText(" jam" );
                        break;
                    case R.id.radio_hari:
                        status.setText(" hari" );
                        break;
                }
            }
        });

        masakanLain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    inputMasakanLain.setVisibility(View.VISIBLE);
                }
                if(!b){
                    inputMasakanLain.setVisibility(View.GONE);
                }
            }
        });
        //button submit
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //radiobutton
                hasilJenisMasakan = "";

                if(masakanBali.isChecked()){
                    hasilJenisMasakan +=masakanBali.getText().toString()+"\n";
                }
                if(masakanIndonesia.isChecked()){
                    hasilJenisMasakan +=masakanIndonesia.getText().toString()+"\n";
                }
                if(masakanEropa.isChecked()){
                    hasilJenisMasakan +=masakanEropa.getText().toString()+"\n";
                }
                if(masakanChina.isChecked()){
                    hasilJenisMasakan +=masakanChina.getText().toString()+"\n";
                }
                if(masakanLain.isChecked()){
                    hasilJenisMasakan +=inputMasakanLain.getText().toString()+"\n";
                }

                int radioId = radioGroup_pilihanMasakan.getCheckedRadioButtonId();
                radioButtonPilihanMasakan = findViewById(radioId);

                resep_nama = namaResep.getText().toString().trim();
                resep_pilihan = radioButtonPilihanMasakan.getText().toString().trim();
                resep_bahan = bahanMasakan.getText().toString().trim();
                resep_langkah = langkahMemasak.getText().toString().trim();
                resep_status_waktu = status.getText().toString().trim();
                resep_waktu = waktu.getText().toString().trim();

                isValidasi = validasi();
                if(isValidasi){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateRecipt.this);
                    builder.setIcon(R.drawable.warning_round);
                    builder.setTitle("Tambah Resep");
                    builder.setMessage(
                            "Apakah kamu sudah selesai dengan resep ini ?\n\n-------------------------------------------------------------------\n\n" +
                                    "Nama Masakan  : " +resep_nama + "\n\n" +
                                    "Lama Memasak  : " +resep_waktu + resep_status_waktu +"\n\n" +
                                    "Pilihan Masakan  : " + resep_pilihan + "\n\n" +
                                    "Jenis Masakan  : \n" + hasilJenisMasakan + "\n" +
                                    "Bahan Masakan  : \n" + resep_bahan + "\n\n" +
                                    "Langkah Memasak  : \n" + resep_langkah + ""
                    );
                    builder.setPositiveButton("Selesai", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // webserver
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https//www.google.com",
                                    new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        insertResepWebserver();
                                        Toast.makeText(getApplicationContext(), "Masakan berhasil kamu simpan !", Toast.LENGTH_SHORT).show();
                                    }
                                }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //sqlite
                                    DbHelper db = new DbHelper(getApplicationContext());
                                    ReciptHandler resepHandler = new ReciptHandler();
                                    resepHandler.setNamaResep(resep_nama);
                                    resepHandler.setLamaMemasak(hasilWaktuMasak);
                                    resepHandler.setStatusLamaMemasak(resep_status_waktu);
                                    resepHandler.setPilihan(resep_pilihan);
                                    resepHandler.setJenis(hasilJenisMasakan);
                                    resepHandler.setBahan(resep_bahan);
                                    resepHandler.setLangkah(resep_langkah);

                                    boolean tambahResep = db.insertData(resepHandler);

                                    if(tambahResep){
                                        Toast.makeText(CreateRecipt.this, "Berhasil Tambah Data", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(CreateRecipt.this, "Gagal Tambah Data", Toast.LENGTH_SHORT).show();
                                    }
                                    Toast.makeText(CreateRecipt.this,"Masakan berhasil kamu simpan !",Toast.LENGTH_SHORT).show();
                                }
                            });
                            RequestQueue queue = Volley.newRequestQueue(CreateRecipt.this);
                            queue.add(stringRequest);
                        }
                    });

                    builder.setNegativeButton(Html.fromHtml("<font color='#777B7E'>Belum</font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
    }

    private boolean validasi(){
        if(namaResep.length() == 0){
            Toast.makeText(CreateRecipt.this, "Nama Masakan Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(radioButtonPilihanMasakan.length() == 0){
            Toast.makeText(CreateRecipt.this, "Pilihan Masakan Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(bahanMasakan.length() == 0){
            Toast.makeText(CreateRecipt.this, "Bahan Masakan Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(langkahMemasak.length() == 0){
            Toast.makeText(CreateRecipt.this, "Langkah Memasak Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!masakanBali.isChecked() && !masakanIndonesia.isChecked() && !masakanEropa.isChecked() && !masakanChina.isChecked() && !masakanLain.isChecked()){
            Toast.makeText(CreateRecipt.this, "Jenis Masakan Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }

    private void insertResepWebserver(){
        String url = Constant.TAMBAH_RESEP;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object.getBoolean("success")){
                                JSONObject resepObject = object.getJSONObject("insert_resep");
                                JSONObject userObject = resepObject.getJSONObject("user");

                                UserHandler user = new UserHandler();
                                user.setId(userObject.getInt("id"));
                                user.setNamaUser(userObject.getString("name"));

                                ReciptHandler resepHandlerList = new ReciptHandler();
                                resepHandlerList.setId(resepObject.getInt("id"));
                                resepHandlerList.setNamaResep(resepObject.getString("nama_resep"));
                                resepHandlerList.setPilihan(resepObject.getString(  "pilihan"));
                                resepHandlerList.setLamaMemasak(resepObject.getString(  "lama_masakan"));
                                resepHandlerList.setStatusLamaMemasak(resepObject.getString(  "status_lama_masakan"));
                                resepHandlerList.setJenis(resepObject.getString(  "jenis"));
                                resepHandlerList.setBahan(resepObject.getString(  "bahan"));
                                resepHandlerList.setLangkah(resepObject.getString(  "langkah"));

                                MainActivity.resepHandler.add(0, resepHandlerList);
                                MainActivity.recyclerView.getAdapter().notifyItemInserted(0);
                                MainActivity.recyclerView.getAdapter().notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), "Masakan berhasil kamu simpan !", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CreateRecipt.this,"Masakan gagal kamu simpan !",Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");
                HashMap<String,String>map = new HashMap<>();
                map.put("Authorization", "Bearer "+token);
                return map;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String>map = new HashMap<>();
                map.put("nama_resep",resep_nama);
                map.put("lama_masakan", hasilWaktuMasak);
                map.put("status_lama_masakan", resep_status_waktu);
                map.put("pilihan", resep_pilihan);
                map.put("jenis", hasilJenisMasakan);
                map.put("bahan", resep_bahan);
                map.put("langkah", resep_langkah);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(CreateRecipt.this);
        queue.add(stringRequest);
    }
}
