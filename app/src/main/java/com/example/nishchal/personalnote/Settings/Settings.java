package com.example.nishchal.personalnote.Settings;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nishchal.personalnote.PinLockActivity;
import com.example.nishchal.personalnote.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {

    private TextView changePassword, pinLockPassword, changePin;
    private FirebaseAuth mAuth;
    private String MY_PREFS_NAME = "pinlock";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        changePassword = findViewById(R.id.change_password);
        pinLockPassword = findViewById(R.id.pin_lock_password);
        changePin = findViewById(R.id.change_pin);
        mAuth = FirebaseAuth.getInstance();

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passChange();
            }
        });
        pinLockPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPinLockPassword();
            }
        });
        changePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePin();
            }
        });

    }

    private void passChange() {

        final Dialog dialog = new Dialog(Settings.this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("Change Password");
        dialog.setContentView(R.layout.dialog_setting_change_password);
        dialog.show();

        final EditText currentPassword = dialog.findViewById(R.id.current_password_field);
        final EditText newPassword = dialog.findViewById(R.id.new_password_field);
        Button save_btn = dialog.findViewById(R.id.change_password_savebtn);

        final FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {

            final String user_email = user.getEmail();

            save_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final ProgressDialog progressDialog = ProgressDialog.show(Settings.this, "Please wait...", "Changing password, please wait...", true);

                    if(user_email != null) {
                        AuthCredential credential = EmailAuthProvider.getCredential(user_email, currentPassword.getText().toString());

                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            user.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        progressDialog.dismiss();

                                                        Toast.makeText(Settings.this, "Password is successfully changed.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        progressDialog.dismiss();
                                                        Log.e("ERROR", task.getException().toString());
                                                        Toast.makeText(Settings.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            });

                                        } else {
                                            progressDialog.dismiss();
                                            Log.e("ERROR", task.getException().toString());
                                            Toast.makeText(Settings.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    } else {
                        Toast.makeText(Settings.this, "Please Sign In again to successfully change your password", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else{
            Toast.makeText(Settings.this, "Please Sign In again to successfully change your password", Toast.LENGTH_SHORT).show();
        }


    }

    private void setPinLockPassword(){

        final Dialog dialog = new Dialog(Settings.this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("Change Password");
        dialog.setContentView(R.layout.dialog_setting_set_pinlock);
        dialog.show();

        final EditText pin = dialog.findViewById(R.id.pin);
        final EditText confirm_pin = dialog.findViewById(R.id.confirm_pin);
        Button set_pin_button = dialog.findViewById(R.id.set_pin_button);

        set_pin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(pin.getText().toString()) == Integer.parseInt(confirm_pin.getText().toString())){
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putInt("Pin", Integer.parseInt(pin.getText().toString()));
                    editor.apply();
                    Toast.makeText(Settings.this, "Your Pin lock is set for app", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(Settings.this, "Pin mismatch", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changePin() {

        final Dialog dialog = new Dialog(Settings.this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("Change Password");
        dialog.setContentView(R.layout.dialog_setting_set_pinlock);
        dialog.show();

        TextView title = dialog.findViewById(R.id.dialog_pin_title);
        final EditText old_pin = dialog.findViewById(R.id.pin);
        final EditText new_pin = dialog.findViewById(R.id.confirm_pin);
        Button set_pin_button = dialog.findViewById(R.id.set_pin_button);

        old_pin.setHint("Old Pin");
        new_pin.setHint("New Pin");
        title.setText("Change Pin Lock");

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final int oldPin = prefs.getInt("Pin", 0);

        set_pin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ipin= Integer.parseInt(old_pin.getText().toString());
                if(ipin == oldPin){
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putInt("Pin", Integer.parseInt(new_pin.getText().toString()));
                    editor.apply();
                    Toast.makeText(Settings.this, "Your Pin lock is successfully changed", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(Settings.this, "Pin mismatch", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
