package com.example.nishchal.personalnote;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nishchal.personalnote.Signin.EmailPasswordSignIn;
import com.nightonke.blurlockview.BlurLockView;
import com.nightonke.blurlockview.Password;

public class PinLockActivity extends AppCompatActivity {

    private BlurLockView blurLockView;
    String MY_PREFS_NAME = "pinlock";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_lock_layout);

        blurLockView = findViewById(R.id.blur_lock_view);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String mypin = Integer.toString(prefs.getInt("Pin", 0));

        blurLockView.setCorrectPassword(mypin);
        blurLockView.setLeftButton("EXIT");
        blurLockView.setRightButton("DELETE");
        blurLockView.setTypeface(Typeface.DEFAULT);
        blurLockView.setType(Password.NUMBER,false);

        blurLockView.setOnLeftButtonClickListener(new BlurLockView.OnLeftButtonClickListener() {
            @Override
            public void onClick() {
                AlertDialog.Builder builder = new AlertDialog.Builder(PinLockActivity.this);
                builder.setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                //alert.setCanceledOnTouchOutside(false);
                alert.show();
            }
        });

        blurLockView.setOnPasswordInputListener(new BlurLockView.OnPasswordInputListener() {
            @Override
            public void correct(String inputPassword) {
                Toast.makeText(PinLockActivity.this, "Correct password", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PinLockActivity.this, EmailPasswordSignIn.class);
                startActivity(i);
            }

            @Override
            public void incorrect(String inputPassword) {
                Toast.makeText(PinLockActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void input(String inputPassword) {

            }
        });

        

    }
}
