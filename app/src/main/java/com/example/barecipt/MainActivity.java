package com.example.barecipt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private DbHelper db;
    public static RecyclerView recyclerView;
    protected RecyclerView.Adapter mainAdapter;
    public static ArrayList<ReciptHandler> resepHandler = new ArrayList<ReciptHandler>();
    private Button btn_edit, btn_delete;
    private SharedPreferences preferences;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DbHelper(this);
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        swipeRefreshLayout = findViewById(R.id.swipeMain);
//        recyclerView.setLayoutManager(mLayoutManager);

//        String stringSetName = preferences.getString("name","");
//        String stringSetToken = preferences.getString("token","");
//        Toast.makeText(getApplicationContext(), stringSetName, Toast.LENGTH_SHORT).show();
//        Log.d("TAG", stringSetToken);
        tampilListData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tampilListData();
            }
        });
//        === sqlite ===
//        final DbHelper dbh = new DbHelper(getApplicationContext());
//        Cursor cursor = dbh.showData();
//        cursor.moveToFirst();
//        if(cursor.getCount()>0){
//            while(!cursor.isAfterLast()){
//                ReciptHandler resepHandlerList = new ReciptHandler();
//                resepHandlerList.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
//                resepHandlerList.setNamaResep(cursor.getString(cursor.getColumnIndexOrThrow("nama_resep")));
//                resepHandlerList.setPilihan(cursor.getString(cursor.getColumnIndexOrThrow("pilihan")));
//                resepHandler.add(resepHandlerList);
//                cursor.moveToNext();
//            }
//        }
//
//        recyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(mLayoutManager);
//        mainAdapter = new MainAdapter(resepHandler, MainActivity.this, recyclerView);
//        recyclerView.setAdapter(mainAdapter);
//        btnDelete = findViewById(R.id.btn_delete);
//        btnEdit = findViewById(R.id.btn_edit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("About BaRecipt");
                builder.setMessage(
                        "BaRecipt merupakan aplikasi pencatatan resep makanan dimana user dapat" +
                                " menginputkan resep masakannya sendiri ke dalam aplikasi yang" +
                                " disimpan ke dalam database\n\n" +
                                "Nama  : I Gede Nyoman Ambara Yasa\n" +
                                "NIM     : 1905551115"
                );
                builder.setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addRecipt(View view) {
        Intent intent = new Intent(MainActivity.this, CreateRecipt.class);
        startActivity(intent);

    }

    private void tampilListData() {
        resepHandler = new ArrayList<>();
        swipeRefreshLayout.setRefreshing(true);
        Log.d("debug1", "masukman");
        StringRequest request = new StringRequest(Request.Method.GET, Constant.RESEP, response ->  {
            Log.d("debug1", "masukpakeko");
            try {
                JSONObject object = new JSONObject(response);
                Log.d("debug1", String.valueOf(object));
                if (object.getBoolean("success")){
                    db.deleteSemuaData();
                    JSONArray array = new JSONArray(object.getString("data"));
                    for(int i=0;i<array.length();i++){
                        JSONObject resepObject = array.getJSONObject(i);
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

                        resepHandler.add(resepHandlerList);
                    }

                    mainAdapter = new MainAdapter(resepHandler, MainActivity.this, recyclerView);
                    recyclerView.setAdapter(mainAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(MainActivity.this,"yo",Toast.LENGTH_SHORT).show();
        }, error -> {
            error.printStackTrace();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(request);
    }
}