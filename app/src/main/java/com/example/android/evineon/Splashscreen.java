package com.example.android.evineon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;



public class Splashscreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splashscreen.this, MainActivity.class);
                Splashscreen.this.startActivity(mainIntent);
                Splashscreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
