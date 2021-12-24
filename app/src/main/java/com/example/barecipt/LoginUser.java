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

public class LoginUser extends AppCompatActivity {
    private EditText email_login, password_login;
    private TextView textView_register;
    private Button btn_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);
        btn_login = findViewById(R.id.btn_login);
        textView_register = findViewById(R.id.link_daftar);

        textView_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginUser.this, RegisterUser.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValidasi = validasi();
                if(isValidasi){
                    StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://103.214.113.148/",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    login();
                                    Toast.makeText(LoginUser.this,"Email atau Password salah !",Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginUser.this,"Koneksi tidak tersambung, harap online terlebih dahulu untuk lanjut!",Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueue queue = Volley.newRequestQueue(LoginUser.this);
                    queue.add(stringRequest);
                }

            }
        });

    }

    private boolean validasi(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        if(email_login.length() == 0){
            Toast.makeText(LoginUser.this, "Email Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(password_login.length() == 0) {
            Toast.makeText(LoginUser.this, "Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }

    private void loginOffline(){
        Intent intentMain = new Intent(LoginUser.this, MainActivity.class);
        startActivity(intentMain);
    }

    private void login(){
        StringRequest request = new StringRequest(Request.Method.POST, Constant.LOGIN, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONObject user = object.getJSONObject("user");
                    SharedPreferences userPref = LoginUser.this.getApplication().getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("token", object.getString("token"));
                    editor.putInt("id", user.getInt("id"));
                    editor.putString("name", user.getString("name"));
                    editor.putString("email", user.getString("email"));
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    Toast.makeText(LoginUser.this, "Login Sukses", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginUser.this, MainActivity.class);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            error.printStackTrace();
        }){
            protected Map<String, String> getParams() throws AuthFailureError{
                HashMap<String, String> map = new HashMap<>();
                map.put("email",email_login.getText().toString().trim());
                map.put("password",password_login.getText().toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(LoginUser.this);
        queue.add(request);
    }
}
