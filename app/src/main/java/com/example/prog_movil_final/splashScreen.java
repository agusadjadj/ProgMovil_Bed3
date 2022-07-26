package com.example.prog_movil_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                //Si existe dato de inicio de sesión
                if (prefs.getString("email",null) != null) {
                    i = new Intent(splashScreen.this, HomeActivity.class);
                } else {
                    i = new Intent(splashScreen.this, AuthActivity.class);
                }
                startActivity(i);
                finish();
            }
        }, 1*1000);

    }

}