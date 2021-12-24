package com.example.barecipt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity {
    private EditText email_register, password_register, nama_register;
    private TextView textView_login;
    private Button btn_register;
    private String email_r, password_r, name_r;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email_register = findViewById(R.id.email_register);
        password_register = findViewById(R.id.password_register);
        nama_register = findViewById(R.id.nama_register);
        btn_register = findViewById(R.id.btn_daftar);
        textView_login = findViewById(R.id.link_login);

        textView_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterUser.this, LoginUser.class);
                startActivity(intent);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValidasi = validasi();
                if(isValidasi){
                    email_r = email_register.getText().toString().trim();
                    Log.d("email masuk", email_r);
                    password_r = password_register.getText().toString();
                    Log.d("pass masuk", password_r);
                    name_r = nama_register.getText().toString();
                    Log.d("nama masuk", name_r);
                    register();
                }
            }
        });
    }

    private boolean validasi(){
        if(nama_register.length() == 0){
            Toast.makeText(RegisterUser.this, "Nama Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(email_register.length() < 8 || password_register.length() > 16){
            Toast.makeText(RegisterUser.this, "Email Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(password_register.length() == 0){
            Toast.makeText(RegisterUser.this, "Password Minimal 8-16 Karakter", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }

    private void register(){
//        Log.d("mamam1", "pertama");
        StringRequest request = new StringRequest(Request.Method.POST, Constant.REGISTER, response -> {
            try {
                JSONObject object = new JSONObject(response);
//                Log.d("mamam2", String.valueOf(object));
                if(object.getBoolean("success")){
                    JSONObject user = object.getJSONObject("user");
                    SharedPreferences userPref = RegisterUser.this.getApplication().getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("token", object.getString("token"));
                    editor.putInt("id", user.getInt("id"));
                    editor.putString("name", user.getString("name"));
                    editor.putString("email", user.getString("email"));
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    Toast.makeText(RegisterUser.this, "Register Sukses", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterUser.this, MainActivity.class);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("email",email_r);
                map.put("password",password_r);
                map.put("name",name_r);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(RegisterUser.this);
        queue.add(request);
    }
}
