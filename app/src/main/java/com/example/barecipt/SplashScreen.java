package com.example.barecipt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //menghilangkan ActionBar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splashscreen);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                Boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

                if(isLoggedIn){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(getApplicationContext(), LoginUser.class));
                    finish();
                }
            }
        }, 3000L); //3000 L = 3 detik
    }
}
