package com.example.nishchal.personalnote.Signin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nishchal.personalnote.MainActivity;
import com.example.nishchal.personalnote.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class EmailPasswordSignIn extends FragmentActivity {

    private FirebaseAuth mAuth;
    private TextView forgot_password;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button signup_dialog, register, signin_button;
    private EditText signup_useremail, signup_userpassword, signin_useremail, signin_userpassword;
    private ImageButton password_show_btn;
    private Boolean password_show_check = false;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        signup_dialog = findViewById(R.id.signup_dialog);
        signin_button = findViewById(R.id.signinbutton);
        forgot_password = findViewById(R.id.forgot_password);

        mAuth = FirebaseAuth.getInstance();

        signin_useremail = findViewById(R.id.signin_emailid);
        signin_userpassword = findViewById(R.id.signin_password);
        password_show_btn = findViewById(R.id.password_show_btn);

        password_show_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!password_show_check) {
                    password_show_btn.setImageResource(R.drawable.ic_visibility);
                    signin_userpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password_show_check = !password_show_check;
                }
                else {
                    password_show_btn.setImageResource(R.drawable.ic_visibility_off);
                    signin_userpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password_show_check = !password_show_check;
                }
            }
        });

        signup_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpAccount();
            }
        });
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailPasswordSignIn();
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    String email = mAuth.getCurrentUser().getEmail();
                   Intent i = new Intent(EmailPasswordSignIn.this, MainActivity.class);
                    i.putExtra("Email", email);
                    startActivity(i);
                }
            }
        };

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //exiting from application on back press
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        moveTaskToBack(true);
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

    private String convertEmailToAstrisks(String email){
        int size = email.length();
        String newEmail1 = email.substring(0, 3);
        String newEmail2 = email.substring(3, size-3);

        StringBuilder astrisks = new StringBuilder(newEmail2.length());
        for (int i = 0; i < newEmail2.length(); i++) astrisks.append('*');

        String newEmail3 = email.substring(size-3, size);
        String newEmail = newEmail1 + astrisks + newEmail3;
        return newEmail;
    }

    private void forgotPassword(){

        String emailStars = convertEmailToAstrisks(signin_useremail.getText().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(EmailPasswordSignIn.this);
        builder.setMessage("Your email to reset password will be send to ("+ emailStars +") this registered email address.")
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAuth.sendPasswordResetEmail(signin_useremail.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(EmailPasswordSignIn.this, "Email is sent to your registered email address.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void emailPasswordSignIn() {

        final ProgressDialog progressDialog = ProgressDialog.show(EmailPasswordSignIn.this, "Please wait...", "Proccessing...", true);
        forgot_password.setVisibility(View.INVISIBLE);
        forgot_password.setPadding(0,0,0,0);

        try{

            (mAuth.signInWithEmailAndPassword(signin_useremail.getText().toString(), signin_userpassword.getText().toString()))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                Intent i = new Intent(EmailPasswordSignIn.this, MainActivity.class);
                                i.putExtra("Email", mAuth.getCurrentUser().getEmail());
                                startActivity(i);
                            } else if(task.getException().getMessage() == "The password is invalid or the user does not have a password."){
                                Log.e("ERROR", task.getException().toString());
                                Toast.makeText(EmailPasswordSignIn.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                forgot_password.setVisibility(View.VISIBLE);
                                forgot_password.setPadding(5,10,5,10);
                            }  else {
                                Log.e("ERROR", task.getException().toString());
                                Toast.makeText(EmailPasswordSignIn.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } catch (Exception e){
            Toast.makeText(EmailPasswordSignIn.this, e.toString(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }

    private void signUpAccount(){

        final Dialog dialog = new Dialog(EmailPasswordSignIn.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.signup_dialog);
        dialog.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Double width = displayMetrics.widthPixels*0.9;
        dialog.getWindow().setLayout(width.intValue(), WindowManager.LayoutParams.WRAP_CONTENT);

        signup_useremail = dialog.findViewById(R.id.signup_emailid);
        signup_userpassword = dialog.findViewById(R.id.signup_password);

        register = dialog.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog progressDialog = ProgressDialog.show(EmailPasswordSignIn.this, "Please wait...", "Processing...", true);

                try{
                    (mAuth.createUserWithEmailAndPassword(signup_useremail.getText().toString(), signup_userpassword.getText().toString()))
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();

                                    if (task.isSuccessful()) {
                                        Intent i = new Intent(EmailPasswordSignIn.this, MainActivity.class);
                                        startActivity(i);
                                    } else {
                                        Log.e("ERROR", task.getException().toString());
                                        Toast.makeText(EmailPasswordSignIn.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                    dialog.dismiss();
                } catch(Exception e) {
                    Toast.makeText(EmailPasswordSignIn.this, e.toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });

    }

}
