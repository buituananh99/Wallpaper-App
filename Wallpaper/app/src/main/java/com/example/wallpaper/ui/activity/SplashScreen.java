package com.example.wallpaper.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.wallpaper.MainActivity;
import com.example.wallpaper.R;
import com.example.wallpaper.common.Common;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(Common.isConnectToInternet(getApplicationContext())){
            new Handler().postDelayed(() -> {
                startActivity(new Intent(this, MainActivity.class));
            }, 3000);
        }else {
            showDialogNoInternet("You are not connect to Internet");
        }

    }

    private void showDialogNoInternet(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage(msg);

        builder.setPositiveButton("OK", (dialog, which) -> {
           new Handler().postDelayed(() -> {
               finish();
           }, 1000);
        });

        builder.setCancelable(false);
        builder.show();

    }
}
