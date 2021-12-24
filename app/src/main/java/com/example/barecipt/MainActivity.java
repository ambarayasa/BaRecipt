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
import android.text.Html;
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
    public static SwipeRefreshLayout swipeRefreshLayout;
    private TextView userNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DbHelper(this);

        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        userNama = findViewById(R.id.userNama);
        userNama.setText((String.valueOf(preferences.getString("name",""))));
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        swipeRefreshLayout = findViewById(R.id.swipeMain);

        tampilListData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
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
            case R.id.logout:
                AlertDialog.Builder logoutBuilder = new AlertDialog.Builder(MainActivity.this);
                logoutBuilder.setTitle("Logout");
                logoutBuilder.setMessage("Yakin ingin logout?");
                logoutBuilder.setPositiveButton(Html.fromHtml("<font color='#E59001'>Yakin</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("isLoggedIn", false);
                        editor.apply();
                        startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    }
                });
                logoutBuilder.setNegativeButton(Html.fromHtml("<font color='#777B7E'>Kembali</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialogLogout = logoutBuilder.create();
                alertDialogLogout.show();
                break;

            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("About BaRecipt");
                builder.setMessage(
                        "BaRecipt merupakan aplikasi pencatatan resep makanan dimana user dapat" +
                                " menginputkan resep masakannya sendiri ke dalam aplikasi yang" +
                                " disimpan ke dalam database\n\n" +
                                "1. Fatliana Kamsia (1905551001)\n" +
                                "2. I Gede Nyoman Ambara Yasa (1905551115)\n" +
                                "3. Ni Komang Sucitra Ardhani (1905551132)\n" +
                                "4. I Dewa Gede Suryadiantha Wedagama (1905551138)\n" +
                                "5. I Made Indra Wahyu Wicaksana (1905551151)\n" +
                                "6. Afrizal Dwi Setiawan (1905551162)"
                );
                builder.setNegativeButton(Html.fromHtml("<font color='#777B7E'>Kembali</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
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
        StringRequest request = new StringRequest(Request.Method.GET, Constant.RESEP, response ->  {
            try {
                JSONObject object = new JSONObject(response);
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
        }, error -> {
            error.printStackTrace();
            swipeRefreshLayout.setRefreshing(false);
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