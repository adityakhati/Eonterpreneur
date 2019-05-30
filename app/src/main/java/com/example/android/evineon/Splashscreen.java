package com.example.android.evineon;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import gr.net.maroulis.library.EasySplashScreen;



public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        EasySplashScreen config = new EasySplashScreen(Splashscreen.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(3000)
                .withBackgroundResource(android.R.color.black
                )
                .withHeaderText("Evineon")
                //.withFooterText("Copyright 2016")
                .withBeforeLogoText("Welcome")
                .withLogo(R.drawable.logo_new);
        //add custom font
       // Typeface pacificoFont = Typeface.createFromAsset(getAssets(), "Pacifico.ttf");
        //config.getAfterLogoTextView().setTypeface(pacificoFont);
        //change text color
        config.getHeaderTextView().setTextColor(Color.WHITE);
        config.getHeaderTextView().setTextSize(15);
        //finally create the view
        View easySplashScreenView = config.create();
        setContentView(easySplashScreenView);
    }
}
