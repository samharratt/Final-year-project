package com.example.home.optometryapplication;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;
//taken from https://www.youtube.com/watch?v=3OwlyWzI-hs
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        EasySplashScreen config = new EasySplashScreen(SplashScreenActivity.this)
                .withFullScreen()
                .withTargetActivity(DashboardActivity.class)
                .withSplashTimeOut(4000)
                .withBackgroundColor(Color.parseColor("#ffffff"))
                .withLogo(R.mipmap.read)
                .withAfterLogoText("Formulary App");

        config.getAfterLogoTextView().setTextColor(Color.BLACK);

        View view = config.create();

        setContentView(view);

    }
}
