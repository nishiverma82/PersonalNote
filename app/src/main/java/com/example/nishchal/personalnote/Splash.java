package com.example.nishchal.personalnote;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.nishchal.personalnote.Signin.EmailPasswordSignIn;

public class Splash extends Activity {

    Intent intent;
    String MY_PREFS_NAME = "pinlock";
    int pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        pin = prefs.getInt("Pin", 0);

        if(pin != 0)
            intent = new Intent(this, PinLockActivity.class);
        else
            intent = new Intent(this, EmailPasswordSignIn.class);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 2000);

    }

}